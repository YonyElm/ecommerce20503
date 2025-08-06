import React from "react";
import {render, screen, waitFor} from "@testing-library/react";
import App from "../App";
import { AuthProvider } from "../context/AuthContext";
import { CartProvider } from "../context/CartContext";

// Inline renderWithProviders helper
const renderWithProviders = (initialRoute = "/") => {
    window.history.pushState({}, "Test page", initialRoute);

    return render(
          <AuthProvider>
              <CartProvider>
                  <App />
              </CartProvider>
          </AuthProvider>
    );
};

describe("App routing", () => {
    test("renders Home page with Navbar and Footer by default", () => {
        renderWithProviders("/");

        expect(screen.getByRole("banner")).toBeInTheDocument(); // MUI AppBar
        expect(screen.getByText(/shop by category/i)).toBeInTheDocument(); // Home content
        expect(screen.getByRole("contentinfo")).toBeInTheDocument(); // MUI Footer
    });

    test("renders Login page", () => {
        renderWithProviders("/login");

        expect(screen.getByRole("banner")).toBeInTheDocument();
        expect(screen.getByText(/Sign In/i)).toBeInTheDocument();
    });

    test("renders Register page", () => {
        renderWithProviders("/register");

        expect(screen.getByRole("banner")).toBeInTheDocument();
        expect(screen.getByText(/Sign Up/i)).toBeInTheDocument();
    });

    test("renders Detail page", async () => {
        renderWithProviders("/details/235");

        expect(screen.getByRole("banner")).toBeInTheDocument();
        await waitFor(() => {
            expect(screen.getByText(/Can't find product/i)).toBeInTheDocument(); // adjust if needed
        });
    });

});
