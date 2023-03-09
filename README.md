# Saga Pattern Microservices

![CircleCI](https://img.shields.io/circleci/build/github/uuhnaut69/saga-pattern-microservices/master?color=green&logo=circleci&style=for-the-badge)
![Maven Central](https://img.shields.io/maven-central/v/org.springframework.boot/spring-boot-starter-parent?color=green&label=spring-boot&logo=spring-boot&style=for-the-badge)
![Docker Image Version (tag latest semver)](https://img.shields.io/docker/v/confluentinc/cp-kafka/6.2.0?color=green&label=confluent&logo=apache-kafka&logoColor=green&style=for-the-badge)

Simple order flow to demo some concepts:

- Microservices using `Spring Boot`, `Spring Cloud`, `Spring Cloud Stream`
- Database per service using `Postgresql`
- Saga Pattern (Saga Orchestration): Distributed transaction span multiple services.
- Outbox Pattern: Avoid dual-writes, no 2PC required using `Kafka`,`Kafka Connect`, `Debezium`
  , `Outbox Event Router`

## Prerequisites

- `Java 19`
- `Docker`
- `Docker-compose`

## Get Started

### Setup environment

```shell
docker-compose up -d
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

| Service's name | Endpoint |
| --- | --- |
|Api Gateway | localhost:8080 |
| Order service | localhost:9090 |
| Customer service | localhost:9091 |
| Inventory service | localhost:9092 |

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

### Todo

- [ ] Authentication
- [ ] K8s deployment (services/infrastructure)
- [ ] Web demo
