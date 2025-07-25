import { useState, useContext, useEffect } from "react";
import { login } from "../api/auth";
import { useNavigate, Link } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";

function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const authContext = useContext(AuthContext);

    // When logged in, login page redirects to home page
    useEffect(() => {
        if (authContext.user) {
            navigate("/");
        }
    }, [authContext.user, navigate]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const res = await login({ email, password });
            authContext.login(res.data.token);
            navigate("/");
        } catch (err) {
            setError("Invalid credentials");
        }
    };

    return (
        <div className="max-w-md mx-auto mt-20 p-6 shadow-xl rounded-xl bg-white">
            <h2 className="text-2xl font-bold mb-4">Login</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type="email"
                    placeholder="Email"
                    className="w-full mb-3 p-2 border rounded"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
                <input
                    type="password"
                    placeholder="Password"
                    className="w-full mb-3 p-2 border rounded"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                {error && <p className="text-red-600 mb-2">{error}</p>}
                <button
                    type="submit"
                    className="w-full bg-cyan-500 text-white py-2 rounded hover:bg-cyan-700"
                >
                    Login
                </button>
            </form>
            <div className="mt-4 text-center">
                <span className="text-gray-700">Don't have an account?</span>{" "}
                <Link to="/register" className="text-teal-700 hover:text-cyan-500 font-medium">
                    Sign up
                </Link>
            </div>
        </div>
    );
}

export default Login;