# Variables
INFRA_COMPOSE := docker-compose.infra.yaml
PROD_COMPOSE := docker-compose.prod.yaml

.PHONY: help infra-up infra-down prod-up prod-down test build clean

help: ## Show this help message
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

## Infrastructure / Development Commands
infra-up: ## Start only the MySQL infrastructure for local development
	docker-compose -f $(INFRA_COMPOSE) up -d

infra-down: ## Stop the infrastructure containers
	docker-compose -f $(INFRA_COMPOSE) down

## Production / Containerized Commands
prod-up: ## Build and start the entire stack (App + DB)
	docker-compose -f $(PROD_COMPOSE) up --build

prod-down: ## Stop the entire production stack
	docker-compose -f $(PROD_COMPOSE) down

## Java / Maven Commands
build: ## Compile and package the Java application locally
	mvn clean package -DskipTests

test: ## Run JUnit tests (Ensure infra-up is running)
	mvn test

clean: ## Remove compiled target files
	mvn clean
	docker system prune -f