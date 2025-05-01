# Device Management API

A RESTful API for managing electronic devices, built with Spring Boot.  
This system allows you to create, configure, retrieve, and delete devices, ensuring proper validation and business rules for device status, availability, and temperature.

## Features

- Create new devices with validation rules.
- Configure device status and temperature with logical constraints.
- Retrieve all available devices sorted by their 7-digit pin code.
- Delete devices by ID.
- Prevent duplicate device pin codes.
- Integrated exception handling and informative responses.
- Unit and integration tests with JUnit and MockMvc.

## Technologies Used

- Java 17+
- Spring Boot
- Spring Data JPA
- H2 / PostgreSQL (configurable)
- JUnit 5
- Mockito
- MockMvc
- Maven

## Getting Started

### Prerequisites

- Java 17 or later
- Maven
- (Optional) PostgreSQL for production-like setup

## 🧪 Step-by-Step Guide: Build, Test, and Run the Application

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/device-management-api.git
cd device-management-api
```

### 2. Configure the Database

Edit `src/main/resources/application.properties`:

#### For H2 in-memory (default for testing):

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
```

#### Or use PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/devices_db
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

### 3. Build the Application

```bash
mvn clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

The application will start on:  
`http://localhost:8080`

### 5. Access H2 Console (if using H2)

Visit:  
`http://localhost:8080/h2-console`  
Use JDBC URL: `jdbc:h2:mem:testdb`

### 6. Test the API

Use Postman or cURL to hit endpoints like:

```bash
curl -X POST http://localhost:8080/api/devices \
  -H "Content-Type: application/json" \
  -d '{"pincode":"1234567","status":1,"availability":true,"temperature":10}'
```

### 7. Run Unit Tests

```bash
mvn test
```

Test classes should be under `src/test/java/...` and named like `DeviceServiceTest.java`, `DeviceControllerTest.java`, etc.

---

