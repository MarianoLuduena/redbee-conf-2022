# kotlin-javalin-seed

## Requirements

- Java 11+

## Build

```shell
docker build -t kotlin-javalin-seed:latest --no-cache .
```

## Run

```shell
docker run --name javalin --rm -it -p 8080:8080 -m 256m --cpus="0.2" kotlin-javalin-seed:latest
```
