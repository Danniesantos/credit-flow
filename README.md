# Credit Flow

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-4169E1)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED)
![Flyway](https://img.shields.io/badge/Flyway-Database_Migrations-CC0200)
![License](https://img.shields.io/badge/License-MIT-blue)

A backend application for credit request and management built with Java and Spring Boot, following Clean Architecture and Domain-Driven Design (DDD) principles.

> 🚧 This project is currently under development.

---

# About

Credit Flow is a study project that simulates a real-world credit management system.

The main goal is to apply software engineering best practices while modeling business rules through a rich domain model instead of relying on an anemic architecture.

Concepts applied in this project:

- Clean Architecture
- Domain-Driven Design (DDD)
- SOLID Principles
- Design Patterns
- Clean Code
- RESTful APIs
- Docker
- Flyway
- Git Best Practices

---

# Technologies

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Flyway
- Docker
- Docker Compose
- Maven
- Bean Validation
- IntelliJ IDEA

---

# Architecture

The project follows the Clean Architecture approach.

```
Controller
        │
        ▼
Application (Use Cases)
        │
        ▼
Domain
        │
        ▼
Infrastructure (Persistence)
```

Current architectural components include:

- Domain Entities
- Value Objects
- Use Cases
- Factories
- Policies
- Repository Interfaces
- Mappers
- DTOs
- Persistence Layer

---

# Project Structure

```
src
├── application
├── domain
├── infrastructure
└── web
```

---

# Features

## Customer Management

The customer module supports:

- Create a customer
- Update customer information
- Retrieve customer by ID
- Retrieve all customers with pagination
- Deactivate customer

### Business Rules

- Customer names must contain at least 3 characters.
- Date of birth cannot be in the future.
- Customers are created with **ACTIVE** status by default.
- A customer cannot be deactivated twice.
- A customer cannot be deactivated while having open credits.
- Updates automatically refresh the last modification timestamp.
- Domain validation is enforced through Value Objects and business rules.

---

## Credit Management

The credit module currently supports:

- Request a new credit
- Simulate a credit
- Analyze a credit
- Retrieve credit details

### Business Rules

- Credit simulation calculates interest before approval.
- Installments are automatically generated.
- The last installment adjusts rounding differences.
- Installment due dates are generated using a due date policy.
- Credits are initially created with **UNDER_ANALYSIS** status.

---

## Installment Management

Installments are automatically created when a credit request is submitted.

Current implementation includes:

- Automatic installment generation
- Monthly due date calculation
- Installment status management
- Precise monetary calculation using BigDecimal
- Rounding adjustment on the last installment

---

# Database

The application uses PostgreSQL as the relational database.

Database versioning is managed using Flyway migrations.

---

# Docker

Start the database using Docker Compose.

```bash
docker compose up -d
```

---

# Environment Configuration

Sensitive information is managed using environment variables.

## 1. Clone the repository

```bash
git clone https://github.com/Danniesantos/credit-flow.git
```

---

## 2. Create a `.env` file

Create a file named:

```
.env
```

at the project root.

Example:

```properties
POSTGRES_DB=creditflow
POSTGRES_USER=admin
POSTGRES_PASSWORD=admin123
```

The `.env` file is ignored by Git and should never be committed.

---

## 3. Copy the example file

The project provides a template:

```
.env.example
```

Copy it to:

```
.env
```

Then replace the placeholder values with your own local configuration.

---

# IntelliJ IDEA Configuration (EnvFile Plugin)

To automatically load environment variables, install the **EnvFile** plugin.

### Install

Settings

↓

Plugins

↓

Marketplace

↓

Search:

```
EnvFile
```

Install and restart IntelliJ IDEA.

---

### Configure

Open:

```
Run
```

↓

```
Edit Configurations
```

Select:

```
CreditFlowApplication
```

Enable:

```
Enable EnvFile
```

Click:

```
+
```

Choose the project's:

```
.env
```

Click:

```
Apply
```

Then:

```
OK
```

---

# Spring Configuration

The application loads database configuration from environment variables.

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5438/${POSTGRES_DB}
    username: ${POSTGRES_USER}
    password: ${POSTGRES_PASSWORD}
```

---

# Docker Compose

Docker Compose uses the same variables.

```yaml
environment:
  POSTGRES_DB: ${POSTGRES_DB}
  POSTGRES_USER: ${POSTGRES_USER}
  POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
```

This keeps Docker and Spring Boot synchronized using a single configuration source.

---

# API

## Customer

| Method | Endpoint | Description |
|----------|------------------------------|--------------------------------------|
| POST | `/customers` | Create a new customer |
| GET | `/customers` | Retrieve all customers |
| GET | `/customers/{id}` | Retrieve customer details |
| PUT | `/customers/{id}` | Update customer information |
| PATCH | `/customers/{id}/status` | Deactivate a customer |

---

## Credit

| Method | Endpoint | Description |
|----------|---------------------------------|--------------------------------|
| POST | `/credits` | Request a new credit |
| POST | `/credits/simulate` | Simulate a credit |
| POST | `/credits/{id}/analyze` | Analyze a credit |
| GET | `/credits/{id}` | Retrieve credit details |

---

# Author

**Daniela Santos**

- GitHub: https://github.com/Danniesantos
- LinkedIn: https://www.linkedin.com/in/daniela-santos-49b434222/
