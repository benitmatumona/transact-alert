# TransactAlert — Asynchronous Fraud Alert Service

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![JMS](https://img.shields.io/badge/JMS-Asynchronous%20Messaging-blue)
![ActiveMQ Artemis](https://img.shields.io/badge/Message%20Broker-ActiveMQ%20Artemis-purple)
![REST](https://img.shields.io/badge/API-REST%2FJSON-lightgrey)

## Overview

TransactAlert is an event-driven fraud detection platform. It accepts bank
transactions over a REST API, hands them off to a message broker instead of
processing them immediately, and lets a separate service check each one for
fraud in the background. When something looks suspicious, an alert is
raised and a third service picks it up to notify a fraud analyst.

The point of building it this way: the API that accepts transactions never
has to wait on fraud-checking logic, so it stays fast and available even if
fraud checking is slow, busy, or briefly down.

## Business Problem

Banks process huge numbers of transactions per second. If fraud checking
happens *inline* — as part of the same request that accepts the
transaction — then:

- customers wait longer for a response
- the ingestion API and the fraud logic are stuck rising and falling together
- a slow or broken fraud check can take down transaction ingestion too

TransactAlert splits these two jobs apart so a problem in one doesn't
become a problem in the other.

## Architecture

```
        Client (bank / payment system)
                    |
                    v
        transaction-service  (REST API)
        - validates the transaction
        - publishes it to a JMS queue
                    |
                    v
        ActiveMQ Artemis (message broker)
        - transaction.queue
                    |
                    v
        fraud-service  (consumer)
        - picks up transactions from the queue
        - runs fraud rules
        - publishes an Alert if something looks wrong
                    |
                    v
        ActiveMQ Artemis
        - fraud.alert.topic
                    |
                    v
        notification-service  (consumer)
        - picks up alerts
        - notifies a fraud analyst
```

`common-library` sits alongside all three services — it holds the shared
`TransactionEvent` and `FraudAlertEvent` classes, so every service agrees
on exactly what a transaction and an alert look like.

## Project Structure

```
transact-alert/
├── common-library/         shared event classes (Transaction, FraudAlert)
├── transaction-service/    REST API — accepts transactions, publishes to queue
├── fraud-service/          consumer — checks fraud rules, publishes alerts
├── notification-service/   consumer — notifies on alerts
├── infrastructure/         docker-compose.yml — starts the Artemis broker
└── README.md
```

## Install / Prerequisites

- Java 17
- Maven
- Docker + Docker Compose (runs the ActiveMQ Artemis broker)

## Running the Project

```bash
# 1. Start the message broker
cd infrastructure
docker compose up -d

# 2. Build the shared library first — the other services depend on it
cd ../common-library
mvn clean install

# 3. Run each service in its own terminal
cd ../transaction-service
mvn spring-boot:run

cd ../fraud-service
mvn spring-boot:run

cd ../notification-service
mvn spring-boot:run
```

## API Usage

**Submit a transaction:**

```
POST /transactions
Content-Type: application/json

{
  "transactionId": "TX1001",
  "customerId": "C001",
  "amount": 85000,
  "currency": "ZAR",
  "merchant": "Online Store"
}
```

**Response (immediate — before fraud checking happens):**

```json
{
  "status": "Transaction accepted for processing"
}
```

Fraud checking and any resulting alert happen asynchronously after this
response is returned.

## Skills Demonstrated

- REST API design with Spring Boot
- Asynchronous, event-driven architecture
- JMS messaging with ActiveMQ Artemis
- Producer/consumer patterns
- Service decoupling across multiple independently-runnable services
- Shared contract design via a common library

## Tech Stack

- Java 17, Spring Boot, Maven
- REST / JSON over HTTP
- JMS, ActiveMQ Artemis
- Docker / Docker Compose

## Roadmap

- [x] Project setup
- [ ] `common-library` — shared event classes
- [ ] `transaction-service` — REST API + JMS producer
- [ ] `fraud-service` — JMS consumer + fraud rules + alert producer
- [ ] `notification-service` — JMS consumer for alerts
- [ ] Persistence (PostgreSQL)
- [ ] Automated tests
