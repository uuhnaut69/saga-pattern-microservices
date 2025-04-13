# Saga Pattern Microservices

![CircleCI](https://img.shields.io/circleci/build/github/uuhnaut69/saga-pattern-microservices/master?color=green&logo=circleci&style=for-the-badge)
![Maven Central](https://img.shields.io/maven-central/v/org.springframework.boot/spring-boot-starter-parent?color=green&label=spring-boot&logo=spring-boot&style=for-the-badge)
![Docker Image Version (tag latest semver)](https://img.shields.io/docker/v/confluentinc/cp-kafka/7.5.0?color=green&label=confluent&logo=apache-kafka&logoColor=green&style=for-the-badge)

Simple order flow to demo some concepts:

- Microservices using `Spring Boot`, `Spring Cloud`, `Spring Cloud Stream`
- Database per service using `Postgresql`
- Saga Pattern (Saga Orchestration): Distributed transaction span multiple services.
- Outbox Pattern: Avoid dual-writes, no 2PC required using `Kafka`,`Kafka Connect`, `Debezium`
  , `Outbox Event Router`

![Banner](./assets/banner.jpg)

## Prerequisites

- `Java 21`
- `Docker`
- `Docker-compose`

## Get Started

### Setup environment

#### AMD

```shell
export PLATFORM=linux/amd64 && docker-compose up -d
```
#### ARM

```shell
export PLATFORM=linux/arm64 && docker-compose up -d
```


### Build projects

```shell
./mvnw clean package -DskipTests=true
```

### Start Services

Run services `api-gateway`, `order-service`, `customer-service`, `inventory-service`

```shell
./mvnw -f api-gateway/pom.xml spring-boot:run
```

```shell
./mvnw -f order-service/pom.xml spring-boot:run
```

```shell
./mvnw -f customer-service/pom.xml spring-boot:run
```

```shell
./mvnw -f inventory-service/pom.xml spring-boot:run
```

| Service's name    | Endpoint       |
| ----------------- | -------------- |
| Api Gateway       | localhost:8080 |
| Order service     | localhost:9090 |
| Customer service  | localhost:9091 |
| Inventory service | localhost:9093 |

### Start outbox connectors

Create outbox connectors

```shell
sh register-connectors.sh
```

### Clean Up

Delete all connectors

```shell
sh delete-connectors.sh
```

### Useful commands

Create new customer

```shell
curl --location 'localhost:8080/customer-service/customers' \
--header 'Content-Type: application/json' \
--data '{
    "username": "usertest",
    "fullName": "Test user 1",
    "balance": 100
}'
```

Create new product

```shell
curl --location 'localhost:8080/inventory-service/products' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Test product 1",
    "stocks": 10
}'
```

Create new order

```shell
curl --location 'localhost:8080/order-service/orders' \
--header 'Content-Type: application/json' \
--data '{
    "customerId": "f0a000ad-ea44-4fa1-853b-207f9d9abe2c",
    "productId": "d212c9d6-da3b-4fe2-b016-a852c8a8d32f",
    "quantity": 1,
    "price": 2
}'
```

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](./LICENSE)
