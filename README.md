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

### Setup API Gateway

```shell
# Create a consumer
curl http://localhost:9180/apisix/admin/consumers -H 'X-API-KEY: edd1c9f034335f136f87ad84b625c8f1' -X PUT -d '
{
    "username": "consumer_1",
    "plugins": {
        "key-auth": {
            "key": "consumer_key_1"
        }
    }
}'

# Create another consumer
curl http://localhost:9180/apisix/admin/consumers -H 'X-API-KEY: edd1c9f034335f136f87ad84b625c8f1' -X PUT -d '
{
    "username": "consumer_2",
    "plugins": {
        "key-auth": {
            "key": "consumer_key_2"
        }
    }
}'

# Create the upstream
curl http://localhost:9180/apisix/admin/upstreams/1 -H 'X-API-KEY: edd1c9f034335f136f87ad84b625c8f1' -X PUT -d '
{
    "keepalive_pool": {
        "idle_timeout": 60,
        "requests": 1000,
        "size": 320
    },
    "name": "upstream_characters",
    "nodes": {
        "apisix-compose-sw-characters-1:8080": 1
    },
    "pass_host": "pass",
    "retries": 0,
    "scheme": "http",
    "timeout": {
        "connect": 60,
        "read": 60,
        "send": 60
    },
    "type": "roundrobin"
}'

# Create the service
curl http://localhost:9180/apisix/admin/services/1 -H 'X-API-KEY: edd1c9f034335f136f87ad84b625c8f1' -X PUT -d '
{
    "name": "service_characters",
    "plugins": {
        "key-auth": {
            "disable": false
        },
        "cors": {
            "allow_origins": "*",
            "allow_methods": "*",
            "allow_headers": "*",
            "expose_headers": "*",
            "max_age": -1,
            "allow_credential": false,
            "disable": false
        },
        "request-id": {
            "disable": false
        }
    },
    "upstream_id": "1"
}'

# Create the route
curl http://localhost:9180/apisix/admin/routes/1 -H 'X-API-KEY: edd1c9f034335f136f87ad84b625c8f1' -X PUT -d '
{
    "name": "route_characters",
    "priority": 0,
    "methods": [
        "GET",
        "HEAD",
        "OPTIONS"
    ],
    "uris": [
        "/characters",
        "/characters/*"
    ],
    "service_id": "1"
}'
```

### Test it!

```shell
curl -vvv 'http://localhost:9080/characters/4' -H 'apikey: consumer_key_1'
```

## Stop

```shell
docker compose down && docker volume prune -f
```
