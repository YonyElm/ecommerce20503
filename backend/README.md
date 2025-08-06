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

### Operational Notes: Order Delivery & Status Tracking

- **Automated Delivery Status:**
  - After purchase, each order item automatically progresses through delivery statuses: `processing` → `shipped` → `delivered`.
  - Status updates occur every 5 minutes, managed by backend scheduled tasks, to simulate real-time order fulfillment.
- **User Actions:**
  - While an item is `processing` or `shipped`, the user may cancel the purchase.
  - Once an item is `delivered`, the user may return it.
  - `cancel` and `return` are final states and will not change unless an admin intervenes.
- **Purpose:**
  - This flow is intentional to reflect a real-world ecommerce experience and to allow reviewers to demo order lifecycle and user/admin actions.

---

## Tech Stack

- **Java 21** (or compatible version)
- **Spring Boot** (REST API, JPA/Hibernate)
- **Maven** (build tool)
- **Lombok** (boilerplate reduction)
- **JWT** (if enabled, for authentication)
- **JUnit** (for backend testing)
- **SQLite** (development, lightweight file-based database)
- **PostgreSQL** (gamma and production, scalable server-based database)

---

## Getting Started

### 1. Prerequisites

- Java (`java -version`) - Should be 21
- Maven (`mvn -v`)

### 2. Install Dependencies

Assuming you are located in `backend` folder

```bash
mvn clean install
```

### 3. Configure Database

- The default configuration uses SQLite (`src/main/resources/schema-sqlite.sql`) for development.
- You can change the database in `src/main/resources/application.properties` if needed or via CLI command with one of the options:
  - dev (default, standalone)
  - gamma (development setting with PostgreSQL, requires running PostgreSQL server on localhost:5432)
  - prod (for AWS deployment, will not work locally)

### 4. Run the Backend

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

The backend server will start at [http://localhost:8080](http://localhost:8080).

* 2 Users are already configured in the system:
  * `admin@example.com` with password `admin`
  * `john@example.com` with password `john_doe`

---

## Project Structure


```
src/main/java/com/example/backend/
├── controller/   # REST controllers (API endpoints for products, cart, orders, users, categories, checkout, etc.)
├── service/      # Business logic and application services (HomeProductsService, OrdersPageService, AdminService, etc.)
├── dao/          # Data access objects (JPA repositories for User, Product, Order, etc.)
├── model/        # JPA entities (User, Product, Order, CartItem, Address, etc.)
├── viewModel/    # View models for frontend integration (DTOs for API responses)
├── utils/        # Utility classes (validation, JWT, password encoding, etc.)
```

---

## API Overview

## API Overview

- **Base URL:** `/api`

### Main Endpoints

#### Authentication
- `POST /api/auth/register` – Register a new user (email, password, fullName)
- `POST /api/auth/login` – Login and receive JWT token

#### Products
- `GET /api/products` – List all products
- `GET /api/products/{productId}` – Get product details
- `GET /api/products/category/{categoryId}` – List products by category
- `POST /api/products` – Add product (admin/seller, supports multipart or JSON)
- `PUT /api/products/{productId}` – Update product (admin/seller)
- `DELETE /api/products/{productId}` – Delete product (admin/seller)

#### Categories
- `GET /api/categories` – List all categories
- `POST /api/categories` – Create category (admin)
- `PUT /api/categories/{categoryId}` – Update category (admin)
- `DELETE /api/categories/{categoryId}` – Delete category (admin)

#### Cart
- `GET /api/cart` – Get current user's cart (requires `userId` header)
- `POST /api/cart/add` – Add/update item in cart (requires `userId` header, productId, quantity)
- `DELETE /api/cart/remove/{cartItemId}` – Remove item from cart

#### Orders
- `GET /api/orders` – Get orders for user (requires `userId` header, `fetchAll` param for admin)
- `PUT /api/orders/{orderItemId}/status` – Update order item status (admin/seller)

#### Checkout
- `GET /api/checkout` – Get checkout data (addresses, payment methods, etc.)
- `POST /api/checkout/cart` – Place order for cart
- `POST /api/checkout/buyitnow` – Place single-product order ("Buy Now")

#### User Settings
- `GET /api/user-settings` – Get user profile, addresses, and payment methods
- `PUT /api/user-settings/profile/name` – Update user full name
- `PUT /api/user-settings/profile/role` – Update user role (admin only)
- `POST /api/user-settings/addresses` – Add address
- `PUT /api/user-settings/addresses/{addressId}` – Update address
- `DELETE /api/user-settings/addresses/{addressId}` – Delete address
- `POST /api/user-settings/payments` – Add payment method
- `PUT /api/user-settings/payments/{paymentId}` – Update payment method
- `DELETE /api/user-settings/payments/{paymentId}` – Delete payment method

#### Users (Admin)
- `GET /api/users` – List all users (admin only)
- `PUT /api/users/activate` – Activate/deactivate user (admin only)

#### Store (Seller/Admin)
- `GET /api/store` – Get products and categories for seller/admin

---

### API Notes
- Most endpoints require a `userId` header for authentication/authorization.
- JWT-based authentication is used for secure endpoints.
- All responses are JSON. Error responses include a message and status code.
- See the [controller](./src/main/java/com/example/backend/controller/) directory for implementation details.

---

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

- **Frontend Integration:** The backend is designed to be consumed by the React frontend in `/frontend` folder.
- **Cross-Origin Requests:** CORS is enabled for local development by default, accepting requests from `localhost:3000`.

---

## License

This project is open-source and available under the [MIT License](../LICENSE).

---