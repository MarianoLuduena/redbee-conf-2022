# Apache APISIX demo (redbee conf - November 2022)

Tested with:

- Kubuntu 22.04
- Docker compose v2.12.2
- Docker client/server v20.10.21

## Build characters microservice

Follow the instructions in the corresponding [readme](kotlin-javalin-seed/README.md).

## Run

Go to `apisix-compose` directory and run compose:

```shell
docker compose up -d
```

## Stop

```shell
docker compose down && docker volume prune -f
```
