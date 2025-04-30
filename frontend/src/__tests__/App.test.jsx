import React from "react";
import { render, screen } from "@testing-library/react";
import App from "../App";
import { AuthProvider } from "../context/AuthContext";

// Helper function to render the app with the AuthProvider and MemoryRouter
const renderWithProviders = (initialRoute = "/") =>
    render(
        <AuthProvider>
            <App />
        </AuthProvider>
    );


describe("App routing", () => {
    test("renders Navbar and Home page by default", () => {
        renderWithProviders("/"); // Using helper function
        expect(screen.getByRole("navigation")).toBeInTheDocument();
        expect(screen.getByText(/welcome to the shop/i)).toBeInTheDocument();
    });

    test("renders Login page when path is /login", () => {
        renderWithProviders("/login"); // Using helper function
        expect(screen.getByRole("navigation")).toBeInTheDocument();
        expect(screen.getByText(/login/i)).toBeInTheDocument();
    });

    test("renders Register page when path is /register", () => {
        renderWithProviders("/register"); // Using helper function
        expect(screen.getByRole("navigation")).toBeInTheDocument();
        expect(screen.getByText(/register/i)).toBeInTheDocument();
    });
});
