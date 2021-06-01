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

### Build projects

```shell
./mvnw clean install package -DskipTests=true
```

### Start Services

Run services `order-service`, `customer-service`, `inventory-service`

| Service's name | Endpoint |
| --- | --- |
| Order service | localhost:9090 |
| Customer service | localhost:9091 |
| Inventory service | localhost:9092 |

### Start outbox connectors

Create outbox order connectors

```shell
curl -X POST http://localhost:8083/connectors \
  -H 'Content-Type: application/json' \
  -H 'Accept: application/json' \
  -d @outbox_order_connector.json
```

Create outbox customer connectors
```shell
curl -X POST http://localhost:8083/connectors \
  -H 'Content-Type: application/json' \
  -H 'Accept: application/json' \
  -d @outbox_customer_connector.json
```

Create outbox inventory connectors
```shell
curl -X POST http://localhost:8083/connectors \
  -H 'Content-Type: application/json' \
  -H 'Accept: application/json' \
  -d @outbox_inventory_connector.json
```

### Test