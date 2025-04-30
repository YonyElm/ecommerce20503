// src/components/Navbar.jsx
import { useContext } from "react";
import { AuthContext } from "../context/AuthContext";
import { Link } from "react-router-dom";

function Navbar() {
    const { user, logout } = useContext(AuthContext);

    return (
        <nav className="flex items-center justify-between px-6 py-4 shadow-md bg-white">
            <Link to="/" className="text-xl font-bold">
                Shop
            </Link>

            <div className="flex items-center space-x-4">
        <span>
          {user ? `Hello, ${user.email}` : "Hi, guest"}
        </span>
                {user ? (
                    <button
                        className="text-sm bg-red-500 text-white px-3 py-1 rounded"
                        onClick={logout}
                    >
                        Logout
                    </button>
                ) : (
                    <>
                        <Link to="/login" className="text-sm text-blue-600">Login</Link>
                        <Link to="/register" className="text-sm text-blue-600">Register</Link>
                    </>
                )}
            </div>
        </nav>
    );
}

export default Navbar;
