// src/components/Navbar.jsx
import { useContext } from "react";
import { AuthContext } from "../context/AuthContext";
import { Link } from "react-router-dom";
import { FaStore, FaShoppingCart } from "react-icons/fa";

function Navbar() {
    const { user, logout } = useContext(AuthContext);

    return (
        <header className="py-3 sticky top-0 z-10 bg-gray-900 text-white shadow-md">
            <div className="container mx-auto flex justify-between items-center px-4">
                {/* Logo with Shop Icon */}
                <Link to="/" className="text-3xl font-bold flex items-center text-cyan-500">
                    <FaStore className="text-3xl mr-2" />
                    <span className="text-xl">
                        <span>E</span>
                        <span className="hidden sm:inline text-white">commerce20503</span>
                        <span className="inline sm:hidden text-white">20503</span>
                    </span>
                </Link>

                {/* Navigation */}
                <nav className="flex items-center space-x-6">
                    {/* Cart */}
                    <Link to="/cart" className="nav-link relative">
                        <FaShoppingCart className="text-2xl" />
                        {/* Replace 3 with dynamic cart count if available */}
                        <span className="cart-count absolute -top-2 -right-2 rounded-full h-5 w-5 flex items-center justify-center text-xs font-bold bg-cyan-500 text-gray-900">
                            3
                        </span>
                    </Link>

                    {/* Account Info */}
                    <div className="nav-link text-sm cursor-pointer">
                        <div className="text-xs">
                            {user ? `Hello, ${user.email}` : "Hello, Sign In"}
                        </div>
                        {user ? (
                            <button
                                className="font-bold text-left focus:outline-none"
                                onClick={logout}
                            >
                                Logout
                            </button>
                        ) : (
                            <div className="font-bold">
                                <Link to="/login">Account & Lists</Link>
                            </div>
                        )}
                    </div>

                    {/* Orders */}
                    <Link to="/orders" className="nav-link text-sm">
                        <div className="text-xs">Returns</div>
                        <div className="font-bold">& Orders</div>
                    </Link>
                </nav>
            </div>
        </header>
    );
}

export default Navbar;
