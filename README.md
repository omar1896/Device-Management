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

### Clone the repository

```bash
git clone https://github.com/your-username/device-management-api.git
cd device-management-api
