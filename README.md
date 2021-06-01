# Saga Pattern Microservices

Simple order flow to demo some concepts:
- Microservices using `Spring Boot`, `Spring Cloud`, `Spring Cloud Stream`
- Database per service using `Postgresql`
- Saga Pattern (Saga Orchestration), Outbox Pattern: Distributed transaction span multiple services and avoid dual-writes using `Kafka`,`Kafka Connect`, `Debezium`

## Get Started

### Setup environment

```shell
docker-compose up -d
```

### Build projects:

```shell
./mvnw clean install package -DskipTests=true
```