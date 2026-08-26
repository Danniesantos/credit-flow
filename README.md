# Credit Flow

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-3.x-6DB33F)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-4169E1)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Messaging-FF6600)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED)
![JUnit 5](https://img.shields.io/badge/JUnit_5-Testing-25A162)
![Testcontainers](https://img.shields.io/badge/Testcontainers-Integration_Testing-2496ED)
![SonarQube](https://img.shields.io/badge/SonarQube-Code_Quality-4E9BCD)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-85EA2D)
![Git](https://img.shields.io/badge/Git-Version_Control-F05032)

A backend REST API for credit request and management, developed with Java 21 and Spring Boot, following Clean Architecture and Domain-Driven Design (DDD) principles.

🚧 This project is a study and portfolio project focused on applying software engineering practices to a realistic credit management domain.

About

CreditFlow simulates a credit management system where customers can request credit, go through a credit analysis process, contract approved credits and manage their installments.

The main goal of the project is to model business rules inside a rich domain model, keeping business logic independent from frameworks and infrastructure concerns.

The project applies concepts such as:

Clean Architecture
Domain-Driven Design (DDD)
SOLID principles
Rich Domain Model
Value Objects
Design Patterns
Domain Events
Use Case pattern
Repository pattern
Mapper pattern
Strategy pattern
Chain of Responsibility
Clean Code
RESTful APIs
Automated testing
Database migrations
Asynchronous messaging
Technologies
Java 21
Spring Boot 3
Spring Data JPA
Hibernate
PostgreSQL
Flyway
RabbitMQ
Docker
Docker Compose
Maven
Bean Validation
JUnit 5
Mockito
AssertJ
Testcontainers
JaCoCo
SonarQube
Git / GitHub
Architecture

The project follows Clean Architecture, separating business rules from application orchestration, web concerns and infrastructure.

                    ┌──────────────────────┐
                    │       Web / REST     │
                    │    Controllers/DTOs  │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │      Application     │
                    │ Use Cases / Services │
                    │ Mappers / Factories  │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │       Domain         │
                    │ Entities / Value     │
                    │ Objects / Rules      │
                    │ Events / Repositories│
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │    Infrastructure    │
                    │ JPA / PostgreSQL     │
                    │ RabbitMQ / Messaging │
                    └──────────────────────┘

The domain layer does not depend on infrastructure implementations.

Current architectural components include:

Domain Entities
Value Objects
Domain Events
Domain Services
Use Cases
Application Services
Factories
Strategies
Validators
Repository Interfaces
Repository Implementations
Mappers
DTOs
Persistence Entities
Messaging Components
Project Structure
src
├── main
│   └── java
│       └── com.daniela.creditflow
│           ├── application
│           ├── domain
│           └── infrastructure
│
└── test
    └── java
        └── com.daniela.creditflow
            ├── application
            ├── domain
            ├── infrastructure
            └── support

The project is organized primarily around architectural boundaries rather than framework-specific layers.

Features
Customer Management

The customer module supports:

Create customer
Update customer information
Retrieve customer by ID
Retrieve customers with pagination
Deactivate customer
Validate customer data
Validate CPF, email and phone number
Manage customer credit score
Manage customer status
Business Rules
Customer name must contain at least 3 characters.
Date of birth cannot be in the future.
Monthly income must be greater than zero.
Credit score must be between 0 and 1000.
Customers are created with ACTIVE status.
A customer cannot be deactivated twice.
A customer cannot be deactivated while having open credits.
Domain validation is performed through Value Objects and business rules.
Credit Management

The credit module supports:

Request credit
Simulate credit
Analyze credit
Contract credit
Retrieve credit details
Retrieve credit balance
Retrieve overdue installments
Find customers with outstanding debts
Cancel credit
Renegotiate credit
Restructure credit
Credit Analysis

Credit analysis is implemented using a validation chain:

Score Validator
       │
       ▼
Income Validator
       │
       ▼
Limit Validator
       │
       ▼
Credit Approved / Rejected

Each validation is responsible for a specific business rule while keeping the analysis flow extensible.

Credit Calculation

Different credit types use different interest calculation strategies:

Personal credit
Payroll credit
Business credit

The calculation strategy is selected through a strategy factory.

CreditCalculationService
          │
          ▼
CreditStrategyFactory
          │
     ┌────┴────┬────────────┐
     ▼         ▼            ▼
 Personal   Payroll      Business
 Strategy   Strategy      Strategy
Credit Adjustments

The system supports two different credit adjustment flows:

Renegotiation

Used for credits with overdue installments. Paid installments are preserved while a new installment schedule is generated for the renegotiated balance.

Restructuring

Used for contracted credits that still have pending installments. A new installment schedule is generated while preserving already paid installments.

Installment Management

Installments are generated when an approved credit is contracted.

Current functionality includes:

Automatic installment generation
Monthly due date calculation
Installment status management
Installment payment
Pending installment calculation
Overdue installment identification
Precise monetary calculations using BigDecimal
Rounding adjustment on the last installment

The installment domain is responsible for its own business behavior instead of exposing only anemic data structures.

Domain Events

The application uses domain events to represent important business occurrences.

Current events include:

CreditApprovedEvent
CreditRejectedEvent
CreditContractedEvent
CreditCanceledEvent
CreditRenegotiatedEvent
CreditRestructuredEvent
InstallmentPaidEvent

Events are published through the application's event infrastructure and can be integrated with asynchronous messaging.

Messaging

RabbitMQ is used for asynchronous communication.

The application includes:

RabbitMQ exchanges
Queues
Retry queues
Dead Letter Queues (DLQ)
Event mapping
Event consumers/producers

The messaging infrastructure is isolated from the domain through application and infrastructure boundaries.

Database

The application uses PostgreSQL as its relational database.

Database schema versioning is managed using Flyway.

The persistence layer uses:

Spring Data JPA
Hibernate
JPA Entities
Persistence Mappers
Repository implementations

Domain models are kept separate from persistence entities.

Docker

Docker Compose is used to run the project's infrastructure locally.

Start the containers with:

docker compose up -d

The Docker environment provides the infrastructure required by the application, such as PostgreSQL and RabbitMQ.

To stop the containers:

docker compose down
Environment Configuration

Sensitive configuration is managed through environment variables.

Create a .env file in the project root based on the provided .env.example.

Example:

POSTGRES_DB=creditflow
POSTGRES_USER=admin
POSTGRES_PASSWORD=admin123

The .env file should not be committed to the repository.

Running the Application
1. Clone the repository
git clone https://github.com/Danniesantos/credit-flow.git
cd credit-flow
2. Configure environment variables

Copy the example environment file:

cp .env.example .env

Adjust the values according to your local environment.

On Windows, the file can also be copied manually.

3. Start infrastructure
docker compose up -d
4. Run the application

Using Maven:

./mvnw spring-boot:run

On Windows:

mvnw.cmd spring-boot:run
API Documentation

The API is documented using OpenAPI/Swagger.

Swagger provides an interactive interface for exploring:

Available endpoints
HTTP methods
Path parameters
Request bodies
Request validation
Response models
HTTP status codes
Business error responses

After starting the application, Swagger UI is available at:

http://localhost:8080/swagger-ui/index.html

The API documentation covers the Customer, Credit and Installment modules.

API

The API follows REST principles.

Customer
Method	Endpoint	Description
POST	/customers	Create a customer
GET	/customers	Retrieve customers
GET	/customers/{id}	Retrieve customer details
PUT	/customers/{id}	Update customer information
PATCH	/customers/{id}/status	Deactivate a customer
Credit
Method	Endpoint	Description
POST	/credits	Request a new credit
POST	/credits/simulate	Simulate a credit
POST	/credits/{id}/analyze	Analyze a credit
POST	/credits/{id}/contract	Contract an approved credit
GET	/credits/{id}	Retrieve credit details
GET	/credits/{id}/balance	Retrieve credit balance
GET	/credits/{id}/overdue	Retrieve overdue installments
GET	/credits/debtors	Retrieve customers with outstanding debts
PATCH	/credits/{id}/cancel	Cancel a credit
POST	/credits/{id}/renegotiate	Renegotiate a credit
POST	/credits/{id}/restructure	Restructure a credit
Installment
Method	Endpoint	Description
POST	/installments/{id}/pay	Pay an installment

The API is under continuous development and new endpoints may be added as business requirements evolve.

Error Handling

The API uses Spring's ProblemDetail for standardized error responses.

HTTP status codes are used according to the type of failure:

400 Bad Request — invalid request data or malformed UUID
404 Not Found — requested resource does not exist
409 Conflict — operation conflicts with the current resource state
422 Unprocessable Entity — request is valid but violates a business rule
500 Internal Server Error — unexpected server-side error

Unexpected exceptions are handled globally without exposing internal implementation details to API consumers.

Testing

The project contains unit, integration and web-layer tests.

Testing technologies include:

JUnit 5
Mockito
AssertJ
Spring Boot Test
MockMvc
Testcontainers

Run the test suite with:

mvn test

To execute the complete Maven verification lifecycle:

mvn clean verify

The project currently contains 400+ automated tests, covering:

Domain rules
Entities
Value Objects
Application services
Use cases
Mappers
Repositories
Controllers
Integration scenarios
Code Quality

Code quality is monitored using SonarQube.

The project uses:

JaCoCo for test coverage
SonarQube for static code analysis
SonarLint during development

Current analysis results include:

Security: A
Reliability: A
Maintainability: A
Test Coverage: 96%+
Duplications: 0%

The project aims to keep business logic well tested and maintainable while continuously improving code quality.

Design Patterns and Practices

The project applies several design patterns and software engineering practices.

Strategy

Used for different credit interest calculation strategies.

CreditInterestCalculationStrategy
├── PersonalCreditStrategy
├── PayrollCreditStrategy
└── BusinessCreditStrategy
Factory

Factories are used to encapsulate object creation and business construction rules.

Chain of Responsibility

Used in the credit analysis process to execute independent validation rules sequentially.

Repository

Domain repositories define contracts while infrastructure provides the persistence implementations.

Mapper

Mappers isolate transformations between:

Domain
   ↕
Application
   ↕
Persistence / Web
Value Objects

The domain uses Value Objects to encapsulate validation and domain concepts such as:

Money
CPF
Email
PhoneNumber
CreditScore
InterestRate
CustomerId
CreditId
InstallmentId
Future Improvements

Possible future improvements include:

Authentication and authorization
Observability and monitoring
Redis caching
Improved messaging observability
CI/CD pipeline
Additional integration tests
Performance improvements
Deployment configuration
Author

Daniela Santos

GitHub: https://github.com/Danniesantos
LinkedIn: https://www.linkedin.com/in/daniela-santos-49b434222/
License

This project is licensed under the MIT License.

---

# License

This project is licensed under the MIT License.

