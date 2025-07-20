import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";
import { AuthProvider } from "./context/AuthContext";
import { CartProvider } from "./context/CartContext";
import reportWebVitals from './reportWebVitals';
import {ThemeProvider} from "@mui/material";
import theme from './theme';

ReactDOM.createRoot(document.getElementById("root")).render(
    // <React.StrictMode> renders components twice in dev mode to catch any errors
    // <React.StrictMode>
    <ThemeProvider theme={theme}>
        <AuthProvider>
            <CartProvider>
                <App />
            </CartProvider>
        </AuthProvider>
    </ThemeProvider>
    // </React.StrictMode>
);

reportWebVitals(console.log);