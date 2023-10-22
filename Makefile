define check-command
  $(if $(shell which ${1}),,$(error Command ${1} does not exist!))
endef

# Unser Basis Command ab Docker > 19
docker-compose = docker compose --project-name level-uphaken

check-requirements:
	$(call check-command,docker)

build-docker: check-requirements
	@$(docker-compose) build --pull
build: build-docker

build-backend:
	@$(docker-compose) exec backend ./gradlew :classes

build-backend-continuous:
	@$(docker-compose) exec backend ./gradlew :classes --continuous

start: check-requirements
	@$(docker-compose) up -d
up: start

start-backend: check-requirements
	@$(docker-compose) up -d backend

start-frontend: check-requirements
	@$(docker-compose) up -d frontend

status:
	@$(docker-compose) ps

stop:
	@$(docker-compose) stop

stop-backend:
	@$(docker-compose) stop backend database

stop-frontend:
	@$(docker-compose) stop frontend

destroy:
	@$(docker-compose) down

restart:
	@$(docker-compose) restart
restart-backend:
	@$(docker-compose) restart backend
restart-frontend:
	@$(docker-compose) restart frontend

logs:
	@$(docker-compose) logs
logs-backend:
	@$(docker-compose) logs backend -f
logs-frontend:
	@$(docker-compose) logs frontend -f

shell: shell-frontend
shell-backend:
	@$(docker-compose) exec backend bash
shell-frontend:
	@$(docker-compose) exec frontend ash

.DEFAULT_GOAL := start
.PHONY: build up start stop status destroy restart restart-backend restart-fontend logs logs-backend logs-frontend shell shell-backend shell-fontend