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

- Java 22+
- Spring Boot
- Spring Data JPA
- H2 / PostgreSQL (configurable)
- JUnit 5
- Mockito
- MockMvc
- Maven

## Getting Started

### Prerequisites

- Java 22 or later
- Maven
- (Optional) PostgreSQL for production-like setup

## 🧪 Step-by-Step Guide: Build, Test, and Run the Application

### 1. Clone the Repository

```bash
git clone https://github.com/omar1896/Device-Management
cd device-management-api
```

### 2. Configure the Database


Edit `src/main/resources/application-test.properties`: 

#### For H2 in-memory (default for testing):

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
```

#### And For PostgreSQL:
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/devices_db
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```
```
-- Create Database
CREATE DATABASE iot_devices
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'English_United States.1256'
    LC_CTYPE = 'English_United States.1256'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;
```
```
-- Create Table: devices
CREATE TABLE IF NOT EXISTS public.devices (
    id bigint NOT NULL,
    availability boolean NOT NULL,
    pincode character varying(255) NOT NULL,
    status smallint NOT NULL,
    temperature integer NOT NULL,
    CONSTRAINT devices_pkey PRIMARY KEY (id),
    CONSTRAINT uk_gsbkxq1rcvw8i4w7qhb2dt6uc UNIQUE (pincode),
    CONSTRAINT devices_status_check CHECK (status >= 0 AND status <= 1)
);
```
```
-- Insert initial test data
INSERT INTO devices (id, pincode, availability, status, temperature) 
VALUES
  (1, '9999999', true, 1, 5),
  (2, '3698521', true, 0, -1),
  (3, '1598743', false, 0, -1),
  (4, '3657894', true, 1, 1),
  (5, '6547531', true, 1, 10);
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

