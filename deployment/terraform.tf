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
  region = "us-west-2" # Replace with your desired region
}

resource "aws_instance" "app_server" {
  ami           = "ami-0c55b9c6e2c09c239" # Replace with the cheapest AMI for your region (e.g., Ubuntu 20.04)
  instance_type = "t2.micro"

  vpc_security_group_ids = [aws_security_group.allow_tls.id]

  user_data = <<-EOF
              #!/bin/bash
              set -e

              sudo apt update
              sudo apt install -y openjdk-17 nginx git unzip vim wget curl postgresql-client maven npm nodejs

              # Clone the repository
              git clone --branch aws-test https://github.com/YonyElm/ecommerce20503 /var/www/ecommerce20503
              
              # Build the React frontend
              cd /var/www/ecommerce20503/frontend
              npm install
              npm run build

              # Copy frontend build
              sudo mkdir -p /var/www/frontend
              sudo cp -r build/* /var/www/frontend/
              
              # Set up Nginx
              sudo cp /var/www/ecommerce20503/deployment/nginx.conf /etc/nginx/sites-available/default
              sudo systemctl restart nginx
              
              # Wait for DB to be ready
              until pg_isready -h ${aws_db_instance.default.address} -p 5432 -U admin; do
                echo "Waiting for database..."
                sleep 5
              done

              # Build and run backend
              cd /var/www/ecommerce20503/backend
              mvn clean package -DskipTests

              java -jar target/*.jar --spring.profiles.active=prod &

              EOF

  tags = {
    Name = "ecommerce-app"
  }
}

resource "aws_security_group" "allow_tls" {
  name        = "allow_tls"
  description = "Allow TLS inbound traffic"
  vpc_id      = aws_vpc.vpc1.id

  ingress {
    description = "TLS from VPC"
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "HTTP from VPC"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "SSH from VPC"
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
    Name = "allow_tls"
  }
}

resource "aws_vpc" "vpc1" {
  cidr_block = "10.0.0.0/16"

  tags = {
    Name = "vpc1"
  }
}

resource "aws_db_instance" "default" {
  allocated_storage = 20
  engine           = "postgres"
  engine_version   = "14.7"
  instance_class   = "db.t3.micro"
  db_name          = "ecommerce_db"
  username         = "admin"
  password         = "admin"
  skip_final_snapshot = true
}

output "website_url" {
  value = "http://${aws_instance.app_server.public_dns}"
  description = "URL of the deployed e-commerce site"
}