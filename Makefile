# Variables
DOCKER_BIN := docker
COMPOSE_BIN := $(DOCKER_BIN) compose
INFRA_FILE := docker-compose.infra.yaml
PROD_FILE := docker-compose.prod.yaml

.PHONY: help infra-up infra-down prod-up prod-down build test clean logs ps

help: ## Show this help message
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

## --- Infrastructure (Dev Mode) ---

infra-up: ## Start MySQL for local development
	$(COMPOSE_BIN) -f $(INFRA_FILE) up -d

infra-down: ## Stop local MySQL
	$(COMPOSE_BIN) -f $(INFRA_FILE) down

infra-logs: ## Follow logs for the dev database
	$(COMPOSE_BIN) -f $(INFRA_FILE) logs -f

## --- Production (Containerized) ---

prod-up: ## Build and run the entire stack (Java + MySQL)
	$(COMPOSE_BIN) -f $(PROD_FILE) up --build -d

prod-down: ## Stop the production stack
	$(COMPOSE_BIN) -f $(PROD_FILE) down -v

## --- Java / Development ---

build: ## Compile and package the Java app using Maven
	mvn clean package -DskipTests

test: ## Run JUnit tests (Requires infra-up)
	mvn test

ps: ## List running containers for this project
	$(COMPOSE_BIN) -f $(INFRA_FILE) ps

clean: ## Clean up build artifacts and Docker volumes
	mvn clean
	$(COMPOSE_BIN) -f $(INFRA_FILE) down -v
	$(DOCKER_BIN) system prune -f