# Quantity Measurement Application

## UC16 – Database Integration with JDBC Persistence

A Java-based **Quantity Measurement Application** that supports multiple measurement operations such as comparison, addition, subtraction, conversion, and division across different measurement units.

**UC16 introduces database persistence using JDBC**, replacing the earlier in-memory storage with a relational database (H2). This enables reliable storage, querying, and testing of measurement operations.

---

# Table of Contents

* [Overview](#overview)
* [Features](#features)
* [Architecture](#architecture)
* [Preconditions](#preconditions)
* [Project Structure](#project-structure)
* [Database Configuration](#database-configuration)
* [Database Schema](#database-schema)
* [Connection Pool](#connection-pool)
* [Repository Implementation](#repository-implementation)
* [Service Layer Integration](#service-layer-integration)
* [Application Workflow](#application-workflow)
* [Example Output](#example-output)
* [Concepts Learned](#concepts-learned)
* [Testing](#testing)
* [GitHub Repository](#github-repository)

---

# Overview

Earlier versions (**UC1 – UC15**) stored measurement operations only in **memory using a cache repository**.

This meant:

* Data was lost when the application restarted.
* No historical tracking was possible.

**UC16 introduces JDBC database persistence**, storing operations in an **H2 relational database**.

This enables:

* Persistent storage of measurement history
* Querying and filtering operations
* Improved scalability
* Reliable integration testing

The application continues to follow a **clean N-Tier architecture** ensuring separation of concerns.

---

# Features

* Compare quantities with different units
* Perform addition and subtraction of measurements
* Convert units across measurement systems
* Divide quantities
* Store measurement operations in database
* Retrieve and analyze historical measurement data
* Secure SQL queries using PreparedStatement
* Reusable database connections using connection pooling
* Switch repository implementations via dependency injection
* Unit and integration testing support

---

# Architecture

The system follows a **layered N-Tier architecture**.

```
Controller Layer
        │
        ▼
Service Layer
        │
        ▼
Repository Layer
        │
        ▼
Database (H2)
```

### Layer Responsibilities

| Layer      | Responsibility                     |
| ---------- | ---------------------------------- |
| Controller | Handles user interaction           |
| Service    | Contains business logic            |
| Repository | Handles data storage and retrieval |
| Entity     | Represents database objects        |

This design improves **maintainability, scalability, and testability**.

---

# Preconditions

Before implementing UC16, the following conditions must be met:

* UC1 – UC15 functionalities implemented
* Project follows **N-Tier architecture**
* `IQuantityMeasurementRepository` interface exists
* `QuantityMeasurementCacheRepository` supports in-memory storage
* `QuantityMeasurementEntity` represents measurement data
* Maven installed
* Java 17 or higher installed
* Basic knowledge of JDBC and SQL
* H2 database available for development/testing

---

# Project Structure

The project follows the **standard Maven directory layout**.

```
src
 ├── main
 │   ├── java
 │   │   └── com.apps.quantitymeasurement
 │   │        ├── controller
 │   │        ├── service
 │   │        ├── repository
 │   │        ├── entity
 │   │        ├── dto
 │   │        ├── model
 │   │        ├── unit
 │   │        ├── util
 │   │        └── exception
 │
 │   └── resources
 │        ├── application.properties
 │        └── db
 │            └── schema.sql
 │
 └── test
      └── java
           └── com.apps.quantitymeasurement
                ├── controller
                ├── service
                ├── repository
                └── integrationTests
```

This separation ensures:

* Clean code organization
* Easier testing
* Better maintainability

---

# Database Configuration

Database configuration is defined in:

```
src/main/resources/application.properties
```

The project uses **H2 in-memory database** for:

* Development
* Unit testing
* Integration testing

Advantages:

* Lightweight
* Fast startup
* No external installation required

---

# Database Schema

The database schema is defined in:

```
src/main/resources/db/schema.sql
```

The schema is automatically loaded when the application starts.

Example table:

```sql
CREATE TABLE quantity_measurement_entity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    measurement_type VARCHAR(50),
    operation_type VARCHAR(50),
    value1 DOUBLE,
    unit1 VARCHAR(20),
    value2 DOUBLE,
    unit2 VARCHAR(20),
    result DOUBLE
);
```

---

# Connection Pool

A **ConnectionPool utility class** manages reusable database connections.

### Responsibilities

* Create multiple connections
* Reuse connections instead of creating new ones
* Improve performance
* Handle concurrent access safely

### Example

```java
Connection connection = ConnectionPool.getConnection();
ConnectionPool.releaseConnection(connection);
```

Benefits:

* Reduced connection overhead
* Better scalability
* Improved application performance

---

# Repository Implementation

A new repository class is introduced:

```
QuantityMeasurementDatabaseRepository
```

This class interacts with the database using **JDBC and SQL queries**.

### Supported Operations

* Save measurement operations
* Retrieve all measurements
* Filter by operation type
* Filter by measurement type
* Count stored measurements
* Delete all measurements

### Example SQL Query

```sql
INSERT INTO quantity_measurement_entity
(measurement_type, operation_type, value1, unit1, value2, unit2, result)
VALUES (?, ?, ?, ?, ?, ?, ?)
```

Using **PreparedStatement** prevents SQL injection vulnerabilities.

---

# Service Layer Integration

The **Service Layer** remains independent of the storage implementation.

Using **Dependency Injection**, it can work with:

```
QuantityMeasurementCacheRepository
```

or

```
QuantityMeasurementDatabaseRepository
```

This ensures:

* Loose coupling
* Easy switching between storage types
* Better testability

---

# Application Workflow

When the application starts:

1. Application initializes
2. Database configuration loads
3. Connection pool is created
4. Repository implementation is selected
5. Measurement operations execute
6. Results are stored in database
7. Resources are closed on shutdown

---

# Example Output

## Application Startup

```
Starting QuantityMeasurementApp...
Using Database Repository
Running Quantity Measurement Operations...
```

## Measurement Operations

```
Compare Result: false
Addition Result: 10.1 CM
Subtraction Result: 9.9 CM
Division Result: 100.0
Conversion Result: 10.0 M
```

## Database Statistics

```
Total measurements stored: 5
All measurements deleted.
Application finished successfully.
```

---

# Concepts Learned

## JDBC (Java Database Connectivity)

JDBC provides a standard API for interacting with relational databases in Java.

---

## Connection Pooling

Maintains reusable database connections to improve performance and scalability.

---

## Parameterized SQL Queries

Using **PreparedStatement** protects applications from SQL injection attacks.

---

## Maven Project Structure

Maven enforces a standard directory layout that improves maintainability.

---

## N-Tier Architecture

Separates application logic into independent layers for scalability and maintainability.

---

## Database Schema Management

SQL schema files allow consistent database initialization across environments.

---

## Integration Testing

Integration tests verify the complete workflow from **controller to database layer**.

---

# Testing

The project includes:

* Unit tests for services
* Repository tests
* Integration tests for database operations

Test directory:

```
src/test/java/com.apps.quantitymeasurement
```

---

# GitHub Repository

View the full project implementation here:

[https://github.com/Saud0786/QuantityMeasurementApp/tree/feature/UC16-JDBCPersistence](https://github.com/Saud0786/QuantityMeasurementApp/tree/feature/UC16-JDBCPersistence)

---
