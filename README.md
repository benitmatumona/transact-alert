# TransactAlert — Asynchronous Fraud Alert Service

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green)
![JMS](https://img.shields.io/badge/JMS-Asynchronous%20Messaging-blue)
![ActiveMQ Artemis](https://img.shields.io/badge/Message%20Broker-ActiveMQ%20Artemis-purple)
![REST](https://img.shields.io/badge/API-REST%2FJSON-lightgrey)
![Docker](https://img.shields.io/badge/Infrastructure-Docker-blue)

## Overview

TransactAlert is a three-service, event-driven fraud detection system built
with Java, Spring Boot, and JMS. It accepts bank transactions over a REST
API, publishes them to a message broker, evaluates each one against
rule-based fraud logic in a fully independent service, and forwards any
flagged transaction to a notification service — all without the ingesting
API ever waiting on fraud analysis.

Every service runs and scales independently. They share no code except a
small common library that defines the message formats they agree on.

## Business Problem

Financial institutions process high volumes of transactions and cannot
afford for fraud analysis to block transaction ingestion. A synchronous
design — where the API waits for a full fraud check before responding —
creates slow customer-facing responses, tight coupling between unrelated
concerns, and poor scalability under load.

TransactAlert solves this by fully separating ingestion from analysis:
each is its own deployable service, and they interact only through
asynchronous messages on a broker.

## Architecture

```
              Client Application
                      |
                      v
            transaction-service (REST API, port 8081)
            - validates the request
            - publishes a TransactionEvent
                      |
                      v
                fraud.queue
                      |
                      v
              fraud-service (JMS consumer)
            - FraudRuleEngine calculates a risk score
            - if score >= 50, publishes a FraudAlertEvent
                      |
                      v
                fraud.alerts
                      |
                      v
           notification-service (JMS consumer)
            - receives the alert, notifies a fraud analyst
```

All messaging runs through an **ActiveMQ Artemis** broker, hosted in Docker.
`common-library` is a shared Maven module holding `TransactionEvent` and
`FraudAlertEvent` — the two message contracts every service depends on, so
producers and consumers never drift out of sync on message shape.

## How It Works

**1. Transaction submitted**

```
POST /transactions
Content-Type: application/json

{
  "customerId": "C004",
  "amount": 25000,
  "merchant": "Unknown Merchant",
  "location": "Bloemfontein"
}
```

`transaction-service` validates the request, generates a transaction ID,
publishes a `TransactionEvent` to `fraud.queue`, and responds immediately:

```
Transaction accepted: 193ebeac-065a-436c-aa3c-3ace05b57587
```

**2. Fraud analysis**

`fraud-service` consumes the event and scores it:

- amount exceeds a defined threshold → +50
- merchant matches a known high-risk pattern → +30

A transaction scoring 50 or higher is published as a `FraudAlertEvent` to
`fraud.alerts`.

**3. Notification**

`notification-service` consumes the alert and represents the point where a
real system would page a fraud analyst or open a case:

```
Notifying fraud analyst: Transaction 193ebeac-... flagged with risk score 80
```

## Project Structure

```
transact-alert/
├── common-library/            shared event contracts
│   └── event/                 TransactionEvent, FraudAlertEvent
├── transaction-service/       REST API
│   ├── controller/            handles POST /transactions
│   ├── model/                 request DTO + validation
│   └── producer/               publishes to fraud.queue
├── fraud-service/             fraud analysis
│   ├── consumer/               listens on fraud.queue
│   ├── rules/                 risk-scoring logic
│   └── producer/               publishes to fraud.alerts
├── notification-service/      alerting
│   └── consumer/               listens on fraud.alerts
├── infrastructure/
│   └── docker-compose.yml     ActiveMQ Artemis broker
└── README.md
```

## Technology Stack

| Layer          | Technology                    |
|-----------------|--------------------------------|
| Language        | Java 17                        |
| Framework       | Spring Boot 3.2.0              |
| Build           | Maven (multi-module)           |
| Messaging       | JMS, ActiveMQ Artemis          |
| API             | REST / JSON over HTTP          |
| Validation      | Jakarta Bean Validation        |
| Infrastructure  | Docker, Docker Compose         |

## Running the Project

```bash
# 1. Start the broker
cd infrastructure
docker compose up -d

# 2. Build the shared library (required by all three services)
cd ../common-library
mvn clean install

# 3. Run each service in its own terminal
cd ../transaction-service   && mvn spring-boot:run
cd ../fraud-service         && mvn spring-boot:run
cd ../notification-service  && mvn spring-boot:run

# 4. Submit a transaction
curl -X POST http://localhost:8081/transactions \
  -H "Content-Type: application/json" \
  -d '{"customerId": "C004", "amount": 25000, "merchant": "Unknown Merchant", "location": "Bloemfontein"}'
```

Watch `fraud-service`'s and `notification-service`'s logs to see the
transaction scored and the alert delivered, live.

## Skills Demonstrated

- Design and implementation of an event-driven, multi-service architecture
- REST API development with Spring Boot, including request validation
- Asynchronous messaging with JMS and ActiveMQ Artemis
- Producer/consumer pattern across three independently deployable services
- Shared contract design via a common Maven module
- Rule-based fraud detection with weighted risk scoring
- Multi-module Maven project structuring
- Debugging real distributed-systems issues: broker authentication,
  classpath/build errors, and transient connection recovery

## Future Improvements

- Persist transactions, alerts, and rule history in PostgreSQL
- Structured logging and centralized error handling
- Automated test coverage (JUnit, Spring Boot Test)
- Containerize all services and orchestrate with Kubernetes
- Expand fraud detection with velocity checks, geolocation anomaly
  detection, and ML-based risk scoring
- Replace `System.out.println` notifications with real integrations
  (email, SMS, or a case-management system)

## Author

Benit Matumona
GitHub: https://github.com/benitmatumona
