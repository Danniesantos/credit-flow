# Credit Flow

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-4169E1)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED)
![Flyway](https://img.shields.io/badge/Flyway-Database_Migrations-CC0200)
![License](https://img.shields.io/badge/License-MIT-blue)

A backend application for credit request and analysis management built with Java and Spring Boot, following Clean Architecture principles and Domain-Driven Design (DDD).

> 🚧 This project is currently under development.

---

# About

Credit Flow is a study project designed to simulate a real-world backend application for managing credit requests.

The project focuses on applying software engineering best practices rather than simply implementing CRUD operations.

Main concepts applied:

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

- Use Cases
- Value Objects
- Factories
- Policies
- Mappers
- DTOs
- Domain Entities
- Repository Interfaces
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

# Features Implemented

## Credit

- Request a new credit
- Simulate credit
- Analyze credit
- Retrieve credit details

## Installments

- Automatic installment generation
- Monthly due date policy
- Installment status management

## Domain

- Rich domain model
- Value Objects
- Encapsulated business rules
- Domain validation
- Factory classes
- Mapping between layers

---

# Database

The project uses PostgreSQL as the relational database.

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
git clone https://github.com/YOUR_USERNAME/credit-flow.git
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

> The `.env` file is ignored by Git and should never be committed.

---

## 3. Use the provided example

Copy:

```
.env.example
```

Rename it to:

```
.env
```

Then update the values according to your local environment.

---

# IntelliJ Configuration (EnvFile Plugin)

To allow Spring Boot to load environment variables automatically, install the **EnvFile** plugin.

## Install the plugin

Open:

```
Settings
```

↓

```
Plugins
```

↓

```
Marketplace
```

Search for:

```
EnvFile
```

Install the plugin and restart IntelliJ IDEA.

---

## Configure EnvFile

Open:

```
Run

↓

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

Select the project's:

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

Now Spring Boot will automatically load all variables from the `.env` file.

---

# Spring Configuration

The application reads database settings from environment variables.

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5438/${POSTGRES_DB}
    username: ${POSTGRES_USER}
    password: ${POSTGRES_PASSWORD}
```

---

# Docker Compose

Docker Compose also uses the same environment variables.

```yaml
environment:
  POSTGRES_DB: ${POSTGRES_DB}
  POSTGRES_USER: ${POSTGRES_USER}
  POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
```

This allows Docker and Spring Boot to share the same configuration.

---

# API

Current endpoints:

| Method | Endpoint | Description |
|----------|-------------------------|--------------------------|
| POST | `/credits` | Request a new credit |
| POST | `/credits/simulate` | Simulate a credit |
| POST | `/credits/{id}/analyze` | Analyze a credit |
| GET | `/credits/{id}` | Retrieve credit details |

---

# Author

**Daniela Santos**

- GitHub: https://github.com/Danniesantos
- LinkedIn: https://www.linkedin.com/in/daniela-santos-49b434222/
