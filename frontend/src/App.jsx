import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import DetailPage from "./pages/DetailPage";
import PrivateRoute from "./components/PrivateRoute";
import Navbar from "./components/NavBar";
import './App.css';
import Footer from "./components/Footer";
import ScrollToTop from "./components/ScrollToTop";
import CartPage from "./pages/CartPage";
import CheckoutPage from "./pages/CheckoutPage";

function App() {
    return (
        <div className="min-h-screen flex flex-col">
            <Router>
                <ScrollToTop />
                <Navbar />
                <main className="flex-1">
                    <Routes>
                        <Route path="/" element={<Home />} />
                        <Route path="/login" element={<Login />} />
                        <Route path="/register" element={<Register />} />
                        <Route path="/details/:productId" element={<DetailPage />} />
                        <Route path="/cart" element={<CartPage />} />
                        <Route path="/checkout" element={<CheckoutPage />} />
                        <Route path="/checkout/:productId" element={<CheckoutPage />} />
                        <Route element={<PrivateRoute />}>
                            {/* Protected routes go here */}
                        </Route>
                    </Routes>
                </main>
                <Footer />
            </Router>
        </div>
    );
}

export default App;