# Deployment Instructions

This directory contains the Nginx and Terraform configurations for deploying the Ecommerce application.

## Prerequisites

*   AWS CLI installed and configured
*   Terraform installed

## Deployment

1.  Initialize Terraform:

    ```bash
    terraform init
    ```

2.  Apply the Terraform configuration:

    ```bash
    terraform apply
    ```

## Destroy Infrastructure

To destroy the infrastructure created by Terraform, run the following command:

```bash
terraform destroy

##

brew install --cask docker
mkdir -p ~/.docker/cli-plugins
brew install docker-compose
ln -sfn $(brew --prefix)/bin/docker-compose ~/.docker/cli-plugins/docker-compose


docker-compose -f docker-compose.yml up  -d