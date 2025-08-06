# Deployment & Infrastructure

This directory contains scripts and configuration for deploying the Ecommerce20503 application, including:

- **Local Gamma development setup** (Docker Compose for PostgreSQL)
- **Production deployment** (Terraform for AWS, Nginx reverse proxy)

---

## Requirements

- **macOS or Linux** (for local dev)
- [Docker](https://www.docker.com/) & [Docker Compose](https://docs.docker.com/compose/) (for local PostgreSQL in Gamma setup)
- [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html) (for production)
- [Terraform](https://www.terraform.io/downloads.html) (for production)
- AWS account with permissions to create EC2, RDS, VPC, etc.

---

## Local Development: PostgreSQL with Docker Compose

For local development (especially with the backend in `gamma` profile), you can run a PostgreSQL database in a Docker container:

1. **Install Docker & Docker Compose:**
   ```bash
   brew install --cask docker
   brew install docker-compose
   mkdir -p ~/.docker/cli-plugins
   ln -sfn $(brew --prefix)/bin/docker-compose ~/.docker/cli-plugins/docker-compose
   ```

2. **Start PostgreSQL:**
   ```bash
   docker-compose -f docker-compose.yml up -d
   ```
   This will start a `postgres:14` container on port 5432 with the credentials set in `docker-compose.yml`.

3. **Configure Backend:**
   - Set your backend profile to `gamma` to use PostgreSQL (see backend README for details).

---

## Nginx Reverse Proxy (Production)

The provided `nginx.conf` is used on the production EC2 server to:
- Serve the React frontend (static files)
- Reverse proxy `/api/` requests to the backend (Spring Boot)
- Serve product assets

**CORS Management:**
All frontend-to-backend traffic is routed through Nginx, so CORS is handled at the proxy layer. The frontend can use `/api` as the base URL in production.

---

## Production Deployment: AWS with Terraform

The `terraform.tf` script provisions the full production stack on AWS:

- **VPC, subnets, security groups**
- **EC2 instance** (Ubuntu, runs both frontend and backend)
- **RDS PostgreSQL database**
- **Nginx** (serves frontend, proxies backend)
- **Route53 DNS** (optional)

### Steps

1. **Install AWS CLI & Terraform**
   - Configure AWS CLI with your credentials: `aws configure`

2. **Initialize Terraform:**
   ```bash
   terraform init
   ```

3. **Apply Terraform (provision infrastructure):**
   ```bash
   terraform apply
   ```
   - This will take ~15 minutes. On completion, Terraform will output the public IP for your app and the RDS endpoint.

4. **Access the App:**
   - Visit the public IP in your browser to access the deployed application.

5. **Destroy Infrastructure:**
   ```bash
   terraform destroy
   ```

---

## Architecture Overview

- **Production:**
  - Both frontend (React build) and backend (Spring Boot JAR) run on the same EC2 Ubuntu instance.
  - Nginx serves static frontend files and proxies `/api/` requests to the backend.
  - Backend connects to a managed PostgreSQL RDS instance.
  - All traffic (frontend and API) is served from the same public IP/domain.

- **Local Development:**
  - Frontend runs on `localhost:3000`, backend on `localhost:8080`.
  - PostgreSQL runs in Docker (`localhost:5432`).
  - CORS is enabled for local development.

---

## Notes

- Make sure your AWS account has sufficient permissions and quota.
- The EC2 instance is provisioned with Java 21, Node.js 18, Maven, Nginx, and all required tools.
- The Terraform script clones the latest code from GitHub and builds both frontend and backend on the server.
- You can customize the Nginx config or Terraform resources as needed for your environment.