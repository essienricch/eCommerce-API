# E-commerce API

## Overview
The E-commerce API is a Spring Boot application built using hexagonal architecture to manage core e-commerce functionalities, including user management, cart operations, order processing, and product handling. It provides a robust backend for an online shopping platform, ensuring clean separation of concerns, testability, and maintainability.

## Features

- **User Management**: Create and retrieve user information with role-based access (e.g., CUSTOMER, ADMIN)
- **Cart Management**: Add items to a cart, update quantities, and retrieve active carts by user
- **Order Processing**: Create and retrieve orders with status tracking (e.g., PENDING)
- **Product Management**: Manage product details and inventory
- **RESTful Endpoints**: Expose API endpoints for front-end integration
- **Unit Testing**: Comprehensive test suite using JUnit and Spring Boot Test

## Technologies

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- H2 Database (in-memory for testing)
- Maven
- JUnit 5
- Hexagonal Architecture

## Prerequisites

- Java 17 or higher
- Maven 3.8.x or higher
- Git

## Setup Instructions

### Clone the Repository:
```bash
git clone https://github.com/essienricch/ecommerce-api.git
cd ecommerce-api
```

### Build the Project:
```bash
mvn clean install
```

### Run the Application:
```bash
mvn spring-boot:run
```

### Run Tests:
```bash
mvn test
```

### Access the API:
The API runs on http://localhost:8050.

Example endpoints (to be implemented):  
- POST `/api/product/create`: Create new Product (ROLE_ADMIN)
- GET `/api/user/{email}`: Retrieve user details
- POST `/api/cart`: Create a new cart
- GET `/api/order/{userId}`: List user orders

## Testing

The project includes unit tests for:
- Repository layer (JpaUserRepositoryTest, JpaCartRepositoryTest, JpaOrderRepositoryTest, JpaProductRepositoryTest)
- Service layer (UserServiceTest, CartServiceTest, OrderServiceTest, ProductServiceTest)
- Application context (EcommerceApiApplicationTests)

Run tests with:
```bash
mvn test
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit changes (`git commit -m "Add your feature"`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a pull request

## License

This project is licensed under the MIT License.
