## Swagger UI

Aby przeglądać dostępne endpointy API, należy przejść pod adres URL: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Docker

To build Docker image of this project you need to build it first using gradle:

```console
./gradlew build
```

Then build Docker image:
```console
docker build -t dean-be  .
```

Then to run container:
```console
docker run -p 8080:8080 --name dean-be dean-be

```