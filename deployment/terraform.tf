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
  ami                         = data.aws_ami.amazon_linux_2.id
  instance_type               = "t2.micro"
  subnet_id                   = aws_subnet.main.id
  vpc_security_group_ids      = [aws_security_group.allow_web.id]
  associate_public_ip_address = true

  user_data = <<-EOF
              #!/bin/bash
              set -e

              sudo yum update
              sudo amazon-linux-extras enable corretto17
              sudo amazon-linux-extras enable nginx1
              sudo amazon-linux-extras enable nodejs12
              sudo yum clean metadata
              sudo yum install -y java-17-amazon-corretto-devel nginx git unzip vim wget curl maven postgresql nodejs
              
              mkdir /home/ec2-user/
              git clone --branch aws-test https://github.com/YonyElm/ecommerce20503 /home/ec2-user/ecommerce20503

              # Build React frontend
              cd /home/ec2-user/ecommerce20503/frontend
              npm install
              npm run build

              # Copy frontend build to NGINX directory
              mkdir -p /home/ec2-user/frontend
              cp -r build/* /home/ec2-user/frontend/

              # Update NGINX config
              sudo mkdir -p /etc/nginx/sites-available/default
              sudo cp /home/ec2-user/ecommerce20503/deployment/nginx.conf /etc/nginx/sites-available/default
              sudo systemctl restart nginx

              # Build and run backend
              cd /home/ec2-user/ecommerce20503/backend
              mvn clean package -DskipTests
              nohup java -jar target/*.jar --spring.profiles.active=prod > /var/log/backend.log 2>&1 &
              EOF

  tags = {
    Name = "ecommerce-demo"
  }
}

# Set an AMI Image - "amazon_linux_2" machine
data "aws_ami" "amazon_linux_2" {
  most_recent  = true
  owners       = ["amazon"]

  filter {
    name   = "name"
    values = ["amzn2-ami-hvm*"]
  }
}

output "app_url" {
  value       = "http://${aws_instance.app_server.public_ip}"
  description = "Access your app in a browser via public IP"
}