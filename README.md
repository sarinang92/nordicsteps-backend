# NordicSteps Backend

This is the backend for **NordicSteps**, an online shoe store. It powers the frontend functionality including product search, filtering, user registration, and order processing.

## Tech Stack
- Java 21  
- Spring Boot 3.4.2  
- PostgreSQL  
- Spring Security  
- Swagger / OpenAPI for documentation  
- MapStruct for object mapping  
- Lombok for reducing boilerplate  

## Getting Started

1. Clone the repository
```bash
git clone <https://github.com/sarinang92/nordicsteps-backend/tree/master>
cd nordic-steps-backend
```

2. Set up the PostgreSQL database

```sql
CREATE DATABASE nordicsteps;
```

3. Configure database connection in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/nordicsteps
spring.datasource.username=Case
spring.datasource.password=Esac
```

4. Build and run
```bash
mvn clean install
mvn spring-boot:run
```

The backend will be available at:
`http://localhost:8080`

## API Documentation

Access Swagger UI at: `http://localhost:8080/swagger-ui.html`
OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### Backend Features

The backend supports:
* Product search and filtering (by user, color, size, price, brand, campaign, area of use)

* User registration and login

* Order creation and viewing

* Authentication and authorization using Spring Security

* Swagger-based API documentation

## Project Structure
src/main/java/com/myproject/
├── config/         # Security and app configuration
├── controller/     # REST API endpoints
├── dto/            # Data Transfer Objects
├── mapper/         # MapStruct mappers
├── model/          # Entity classes
├── repository/     # JPA repositories
├── service/        # Business logic
└── exception/      # Global exception handling

## API Endpoints