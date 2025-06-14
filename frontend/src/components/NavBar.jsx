import { useContext } from "react";
import { useNavigate, Link } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import { CartContext } from "../context/CartContext";
import { FaStore, FaShoppingCart } from "react-icons/fa";

function Navbar() {
    const { user, logout } = useContext(AuthContext);
    const { cartItems } = useContext(CartContext);
    const navigate = useNavigate();

    // Logout + Route Home
    const handleLogout = () => {
        logout();
        navigate("/");
    };

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
                    <Link to={user ? "/cart" : "/login"} className="relative">
                        <FaShoppingCart className="text-2xl" />
                        <span className="absolute -top-2 -right-2 rounded-full h-5 w-5 flex items-center justify-center text-xs font-bold bg-cyan-500 text-gray-900">
                            {user && cartItems?.length ? cartItems?.length : 0}
                        </span>
                    </Link>

                    {/* Account Info */}
                    <div className="text-sm cursor-pointer">
                        <div className="text-xs">
                            {user && user.email ? `Hello, ${user.email}` : "Hello, Sign In"}
                        </div>
                        {user ? (
                            <button
                                className="font-bold text-left focus:outline-none"
                                onClick={handleLogout}
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
                    <Link to={user ? "/orders" : "/login"} className="text-sm">
                        <div className="text-xs">Returns</div>
                        <div className="font-bold">& Orders</div>
                    </Link>
                </nav>
            </div>
        </header>
    );
}

export default Navbar;