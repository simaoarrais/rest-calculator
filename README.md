# Java Challenge: Distributed Calculator

This project implements a distributed calculator with a **REST API** and a **Kafka-based communication layer** between two Spring Boot modules: `rest` and `calculator`.

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.6-green)
![Kafka](https://img.shields.io/badge/Kafka-3.8.1-orange)

---

## 📦 Modules

- **rest**: Exposes a RESTful API with endpoints for sum, subtraction, multiplication, and division.
- **calculator**: Listens for calculation requests via Kafka and returns results back via Kafka.

---

## 🚀 Features

- Operations: `sum`, `subtraction`, `multiplication`, `division`
- Support for **arbitrary-precision signed decimal numbers** using `BigDecimal`
- **Apache Kafka** for inter-module messaging
- **Spring Boot** + **Maven** structure
- **Global error handling** with JSON responses
- **Dockerized** with Docker Compose
- **Unit tests** for service logic, REST controller, Kafka listener/producer
- **SLF4J logging** with request lifecycle tracking
- **MDC propagation** across Kafka messages using a unique request ID
- **Logs persisted** to `/app/logs/app-{yyyy-MM-dd}.log` inside each container

---

## 📋 Requirements

- Java 21
- Maven 3.8.x+
- Docker + Docker Compose

---

## ⚙️ Build Instructions

1. **Clone the repository:**
   ```bash
   git clone https://github.com/simaoarrais/rest-calculator.git
   cd rest-calculator
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Build Docker images:**
   ```bash
   docker compose build
   ```

4. **Run the application:**
   ```bash
   docker compose up
   ```

---

## 🌐 API Endpoints

| Method | Endpoint             | Description                 |
|--------|----------------------|-----------------------------|
| GET    | `/sum`               | `?a=...&b=...`              |
| GET    | `/subtraction`       | `?a=...&b=...`              |
| GET    | `/multiplication`    | `?a=...&b=...`              |
| GET    | `/division`          | `?a=...&b=...`              |

Example:

```http
GET http://localhost:8080/sum?a=2&b=3
Response: { "result": 5 }
```

---

## 🧪 Run Unit Tests

```bash
mvn test
```

Tests cover:
- REST endpoints
- Service logic (math operations)
- Kafka listeners and producers
- Edge cases like division by zero, invalid input, missing parameters

---

## 📈 Test Coverage

Generate a JaCoCo report:

```bash
mvn clean test jacoco:report
```

Open the report:

```bash
xdg-open rest/target/site/jacoco/index.html  # rest
xdg-open calculator/target/site/jacoco/index.html  # calculator
```

---

## 👤 Developer

- **Name**: Simão Arrais  
- **Email**: simaoarrais@gmail.com  
- **GitHub**: [@simaoarrais](https://github.com/simaoarrais)
