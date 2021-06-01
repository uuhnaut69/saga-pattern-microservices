# Saga Pattern Microservices

Simple order flow to demo some concepts:

- Microservices using `Spring Boot`, `Spring Cloud`, `Spring Cloud Stream`
- Database per service using `Postgresql`
- Saga Pattern (Saga Orchestration), Outbox Pattern: Distributed transaction span multiple services and avoid
  dual-writes using `Kafka`,`Kafka Connect`, `Debezium`

## Get Started

### Setup environment

```shell
docker-compose up -d
```

### Build projects

```shell
./mvnw clean install package -DskipTests=true
```

### Start Services

Run services `order-service`, `customer-service`, `inventory-service`

```shell
./mvnw -f order-service/pom.xml spring-boot:run
```

```shell
./mvnw -f customer-service/pom.xml spring-boot:run
```

```shell
./mvnw -f inventory-service/pom.xml spring-boot:run
```

| Service's name | URL |
| --- | --- |
| Order service | localhost:9090 |
| Customer service | localhost:9091 |
| Inventory service | localhost:9092 |

### Start outbox connectors

Create outbox connectors

```shell
sh register-connectors.sh
```

### Test