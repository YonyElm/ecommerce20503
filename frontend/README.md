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
├── api/            # API utility functions (communicates with backend)
├── components/     # React components (ProductList, Cart, Checkout, etc.)
├── pages/          # Page-level components/views (Home, ProductDetail, CheckoutPage, etc.)
├── App.js          # Main application component
├── index.js        # Entry point
```

---

## Core Features

- **Product Browsing:** View and search products by category.
- **Product Details:** View detailed product information.
- **User Authentication:** Login and register (connected to Spring Boot backend).
- **Shopping Cart:** Add, remove, and update product quantities.
- **Checkout:** Select shipping address and payment method, place orders.
- **Order History:** View past orders (requires login).

---

## Environment Variables

If needed, create a `.env` file in the `frontend/` directory to override default API endpoints, e.g.:
```
REACT_APP_API_BASE_URL=http://localhost:8080/api
```

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
