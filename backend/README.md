# Ecommerce20503 Backend

This is the **Spring Boot backend** for the Ecommerce20503 project, an e-commerce web application with a [React frontend](../frontend/README.md).  
The backend provides RESTful APIs for product browsing, shopping cart, checkout, user authentication, order management, and more.

---

## Features

- **User Management:** Registration, authentication, roles, and secure sessions
- **Product Catalog:** Category, product, and inventory APIs
- **Shopping Cart:** Cart and cart item management per user
- **Order Processing:** Checkout, order creation, and order history
- **Payments & Shipping:** Payment methods and shipping address APIs
- **Extensible Database Design:** Easily add new features via well-structured models and DAOs

---

## Tech Stack

- **Java 17+** (or compatible version)
- **Spring Boot** (REST API, JPA/Hibernate)
- **Maven** (build tool)
- **SQLite** (default, easily swappable)
- **Lombok** (boilerplate reduction)
- **JWT** (if enabled, for authentication)
- **JUnit** (for backend testing)

---

## Getting Started

### 1. Prerequisites

- Java (`java -version`)
- Maven (`mvn -v`)

### 2. Install Dependencies

```bash
cd backend
mvn clean install
```

### 3. Configure Database

- The default configuration uses SQLite (`src/main/resources/schema-sqlite.sql`) for development.
- You can change the database in `src/main/resources/application.properties` if needed.

### 4. Run the Backend

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=gamma"
```

The backend server will start at [http://localhost:8080](http://localhost:8080).

---

## Project Structure

```
src/main/java/com/example/backend/
├── controller/   # REST controllers (API endpoints)
├── service/      # Business logic services
├── dao/          # Data access objects (JPA repositories)
├── model/        # JPA entities (User, Product, Order, etc.)
├── viewModel/    # View models for frontend integration
```

---

## API Overview

- **Base URL:** `/api`
- **Key Endpoints:**
    - `/api/products` – Product catalog
    - `/api/categories` – Product categories
    - `/api/cart` – Shopping cart management
    - `/api/orders` – Order creation and history
    - `/api/checkout` – Checkout page data (shipping & payment options)
    - `/api/auth` – User login and registration

Explore the code in the [controller](./src/main/java/com/example/backend/controller/) directory for details.

---

## Testing

To run backend tests:

```bash
mvn test
```

---

## Debugging

To enable remote debugging (port 5005):

```bash
mvn spring-boot:run \
  -Dspring-boot.run.arguments="--spring.profiles.active=dev" \
  -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
```

Attach your IDE to `localhost:5005`.

---

## Development Tips

- **Frontend Integration:** The backend is designed to be consumed by the React frontend in `/frontend`.
- **Cross-Origin Requests:** CORS is enabled for local development by default.
- **API Docs:** Consider adding Swagger/OpenAPI for easier API exploration.

---

## License

This project is open-source and available under the [MIT License](../LICENSE).

---