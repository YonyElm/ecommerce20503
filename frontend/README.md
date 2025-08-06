# Ecommerce20503 Frontend

This is the React frontend for the **Ecommerce20503** project, a full-stack ecommerce web application with a Spring Boot backend.  
The frontend provides a user-friendly interface for browsing products, managing a shopping cart, and completing purchases.

---

## Getting Started

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

### Prerequisites

- [Node.js](https://nodejs.org/) (version 14 or above recommended)
- [npm](https://www.npmjs.com/) (comes with Node.js)

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/YonyElm/ecommerce20503.git
   cd ecommerce20503/frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

### Running the Development Server

```bash
npm start
```

- The app will be available at [http://localhost:3000](http://localhost:3000)
- Make sure the backend is running (by default, on http://localhost:8080) for full functionality.

---

## Project Structure


```
src/
├── api/            # API utility functions (Axios config, endpoints for products, cart, orders, users, etc.)
├── components/     # Reusable React components (ProductCard, CartItem, NavBar, Footer, modals, etc.)
├── context/        # React Context providers and hooks (Auth, Cart, Orders, Category, etc.)
├── pages/          # Page-level components/views (Home, ProductDetail, CartPage, CheckoutPage, OrdersPage, AdminPage, etc.)
├── utils/          # Utility functions (e.g., API validators)
├── __tests__/      # Unit and integration tests for components and context
├── App.jsx         # Main application component
├── index.js        # Entry point
```

---

## Core Features

- **Product Browsing:** View and filter products by category on the Home page.
- **Product Details:** View detailed product information, including images, price, and stock status.
- **User Authentication:** Login and register with JWT-based authentication (integrated with Spring Boot backend).
- **Shopping Cart:** Add, remove, and update product quantities; cart persists for guests and logged-in users.
- **Checkout:** Select shipping address and payment method, place orders (supports both cart and "Buy Now").
- **Order History:** View past orders and order details (requires login).
- **User Settings:** Manage profile, addresses, and payment methods.
- **Admin & Seller Features:** Admin dashboard for managing users, products, orders, and categories; sellers can manage their own products.
- **Responsive Design:** Fully responsive UI using Material-UI and Tailwind CSS.


### Operational Notes & Demo Experience

- **Fast Onboarding:** For demo and POC purposes, there are no checks or limits on password length or email format during registration/login. Any input will work, making it easy to try the system instantly.
- **Photo Uploads:** Product image uploads are limited to 0.5MB per file. Larger files will be rejected.
- **Mock Data Entry:** Adding a new credit card or address in the UI will accept any value. These are for demo/mock experience only and are not validated or used for real transactions.

These choices are intentional to maximize ease of use and speed for reviewers and demo users.

---

## Environment Variables

If needed, create a `.env` file in the `frontend/` directory to override default API endpoints, e.g.:
```
REACT_APP_API_BASE_URL=http://localhost:8080/api
```

---

## API & Client Configuration

- The frontend consumes REST APIs exposed by the backend (Spring Boot).
- All API requests are made via [Axios](https://axios-http.com/) using a pre-configured instance in `src/api/axios.js`.
- The base URL for API requests is set by the `REACT_APP_API_BASE_URL` environment variable (defaults to `http://localhost:8080/api`).
- JWT tokens are stored in `localStorage` and automatically attached to API requests via an Axios interceptor.
- Authentication state and user info are managed via React Context (`src/context/AuthContext.jsx`).
- API modules in `src/api/` provide functions for products, cart, orders, users, categories, checkout, and user settings.
- Error handling and loading states are managed in context and page components.

---

## Scripts

- `npm start` – Runs the app in development mode.
- `npm test` – Launches the test runner.
- `npm run build` – Builds the app for production.
- `npm run eject` – Ejects the configuration (not recommended).

---

## Customization

You can customize the frontend by editing components in the `src/components/` and `src/pages/` directories.  
API requests are handled in the `src/api/` directory.

---

## Learn More

- [React documentation](https://reactjs.org/)
- [Create React App documentation](https://facebook.github.io/create-react-app/docs/getting-started)
- [Spring Boot backend (in this repo)](../backend/README.md)

---

## License

This project is open-source and available under the [MIT License](../LICENSE).
