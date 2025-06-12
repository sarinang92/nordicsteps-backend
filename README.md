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

```
src/main/java/com/myproject/
├── config/         # Security and app configuration
├── controller/     # REST API endpoints
├── dto/            # Data Transfer Objects
├── mapper/         # MapStruct mappers
├── model/          # Entity classes
├── repository/     # JPA repositories
├── service/        # Business logic
└── exception/      # Global exception handling
```

## API Endpoints

The backend follows RESTful conventions and is organized into resources like users, products, cart, orders, and authentication.

### User

- `GET /api/v1/users` — Retrieve all users  
- `GET /api/v1/users/{id}` — Retrieve a user by ID  
- `POST /api/v1/users/register` — Register a new user  
- `PUT /api/v1/users/{id}` — Update a user  
- `DELETE /api/v1/users/{id}` — Delete a user  

### Products

- `GET /api/v1/products` — Retrieve all products  
- `GET /api/v1/products/{id}` — Retrieve a product by ID  
- `GET /api/v1/products/search` — Filter products by color, size, brand, campaign, etc.  
- `POST /api/v1/products` — Create a new product  
- `PUT /api/v1/products/{id}` — Update a product  
- `DELETE /api/v1/products/{id}` — Delete a product  

### Cart

- `GET /api/v1/cart/items` — Retrieve all cart items  
- `GET /api/v1/cart/items/{id}` — Retrieve a specific cart item  
- `POST /api/v1/cart/items` — Add an item to the cart  
- `PUT /api/v1/cart/items/{id}` — Update an item's quantity  
- `DELETE /api/v1/cart/items/{id}` — Remove an item from the cart  
- `DELETE /api/v1/cart/clear` — Clear the current user's cart  

### Order

- `GET /api/orders/{orderId}` — Retrieve an order by ID  
- `GET /api/orders/user/{userId}` — Retrieve all orders for a user  
- `POST /api/orders/checkout/{userId}` — Place a new order  
- `PATCH /api/orders/{orderId}/status` — Update an order's status 

### Authentication

- `POST /api/auth/login` — Log in a user  
- `POST /api/auth/change-password` — Change the user's password  


## Test Coverage Report

We used JaCoCo (Java Code Coverage) to measure how much of the backend code is covered by tests.
* The full HTML report is located at: `target/site/jacoco/index.html`

| Package                    | Coverage |
| -------------------------- | -------- |
| `com.myproject.config`     | 74%      |
| `com.myproject.service`    | 8%       |
| `com.myproject.model`      | 7%       |
| `com.myproject.controller` | 14%      |
| `com.myproject.exception`  | 21%      |
| `com.myproject.mapper`     | 1%       |
| `com.myproject.dto`        | 0%       |
| **Overall**                | **4%**   |