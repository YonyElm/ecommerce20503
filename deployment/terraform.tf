terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0"
    }
  }
  required_version = ">= 0.14.9"
}

provider "aws" {
  region = "us-west-2" # Adjust as needed
}

# VPC
resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"
  enable_dns_support   = true
  enable_dns_hostnames = true
  tags = {
    Name = "main-vpc"
  }
}

# Subnet
resource "aws_subnet" "main" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.1.0/24"
  map_public_ip_on_launch = true
  availability_zone       = "us-west-2a"
  tags = {
    Name = "main-subnet"
  }
}

resource "aws_subnet" "subnet_az_backup_rule" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.2.0/24"
  map_public_ip_on_launch = true
  availability_zone       = "us-west-2b"
}

# Internet Gateway
resource "aws_internet_gateway" "gw" {
  vpc_id = aws_vpc.main.id
  tags = {
    Name = "main-gw"
  }
}

# Route Table
resource "aws_route_table" "rt" {
  vpc_id = aws_vpc.main.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.gw.id
  }
}

# Associate Route Table with Subnet
resource "aws_route_table_association" "rta" {
  subnet_id      = aws_subnet.main.id
  route_table_id = aws_route_table.rt.id
}

# Security Group
resource "aws_security_group" "allow_web" {
  name        = "allow_web"
  vpc_id      = aws_vpc.main.id

  ingress {
    description = "Allow HTTP"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Allow SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "allow_web"
  }
}

# EC2 Instance
resource "aws_instance" "app_server" {
  ami                         = data.aws_ami.ubuntu-22.id
  instance_type               = "t3.medium" # 2 vCPUs, 4 GiB RAM needed for the application
  subnet_id                   = aws_subnet.main.id
  vpc_security_group_ids = [
    aws_security_group.allow_web.id,
    aws_security_group.db_sg.id
  ]
  associate_public_ip_address = true

  user_data = <<-EOF
              #!/bin/bash
              set -e

              sudo apt update && sudo apt upgrade -y
              sudo apt install -y nginx git unzip vim wget curl maven postgresql-client openjdk-21-jdk
              sleep 1
              sudo curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
              sleep 1
              sudo apt install -y nodejs
              java -version
              node -v
              npm -v

              git clone --branch aws-test https://github.com/YonyElm/ecommerce20503 /home/ubuntu/ecommerce20503

              # Build React frontend
              cd /home/ubuntu/ecommerce20503/frontend
              npm install
              NODE_OPTIONS="--max_old_space_size=2048" npm run build

              # Give execute (x) permission to traverse folders
              sudo chmod o+rx /home
              sudo chmod o+rx /home/ubuntu
              sudo chmod o+rx /home/ubuntu/ecommerce20503
              sudo chmod o+rx /home/ubuntu/ecommerce20503/frontend
              sudo chmod -R o+rx /home/ubuntu/ecommerce20503/frontend/build

              # Update NGINX config
              sudo mkdir -p /etc/nginx/sites-available/
              sudo cp /home/ubuntu/ecommerce20503/deployment/nginx.conf /etc/nginx/sites-available/default
              sudo systemctl restart nginx

              # Wait for DB to be ready
              until pg_isready -h ${aws_db_instance.default.address} -p 5432 -U admin; do
                echo "Waiting for database..."
                sleep 5
              done

              # Build and run backend
              cd /home/ubuntu/ecommerce20503/backend
              mvn clean package -DskipTests
              nohup java -jar target/*.jar --spring.profiles.active=prod > backend.log 2>&1 &
              EOF

  tags = {
    Name = "ecommerce-demo"
  }
}

# Set an AMI Image - "ubuntu-22" machine
data "aws_ami" "ubuntu-22" {
  most_recent = true
  owners      = ["099720109477"] # Canonical (Ubuntu) official owner ID

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"]
  }
}

data "aws_rds_engine_version" "postgres_14" {
  engine  = "postgres"
  version = "14"
  default_only = true
}

resource "aws_db_instance" "default" {
  identifier         = "ecommerce-db"
  engine             = "postgres"
  engine_version     = data.aws_rds_engine_version.postgres_14.version  # Use the dynamic value
  instance_class     = "db.t3.micro"    # Free tier compatible
  allocated_storage  = 20
  storage_type       = "gp2"
  username           = "ecommerce20503"
  password           = "ecommerce20503"          
  db_name            = "ecommerce_db"
  publicly_accessible = true            # Or false if private subnet
  vpc_security_group_ids = [aws_security_group.db_sg.id]
  db_subnet_group_name = aws_db_subnet_group.default.name
  skip_final_snapshot = true

  tags = {
    Name = "ecommerce-db"
  }
}

resource "aws_security_group" "db_sg" {
  name        = "allow_db_access"
  vpc_id      = aws_vpc.main.id

  ingress {
    description = "Allow Postgres from EC2"
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    security_groups = [aws_security_group.allow_web.id]  # Allow from EC2's SG
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_db_subnet_group" "default" {
  name       = "my-db-subnet-group"
  subnet_ids = [aws_subnet.main.id, aws_subnet.subnet_az_backup_rule.id]

  tags = {
    Name = "My DB subnet group"
  }
}

resource "aws_route53_record" "rds_cname" {
  zone_id = data.aws_route53_zone.main.zone_id
  name    = "db.ecommerce20503.com"
  type    = "CNAME"
  ttl     = 300
  records = [aws_db_instance.default.address]
}

data "aws_route53_zone" "main" {
  name         = "ecommerce20503.com"
  private_zone = false
}

output "app_url" {
  value       = "http://${aws_instance.app_server.public_ip}"
  description = "Access your app in a browser via public IP"
}

output "rds_endpoint" {
  value       = aws_db_instance.default.address
  description = "PostgreSQL RDS endpoint"
}