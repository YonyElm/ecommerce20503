import { useState, useContext } from "react";
import { login } from "../api/auth";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";

function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const authContext = useContext(AuthContext);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const res = await login({ username, password });
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
                    type="text"
                    placeholder="Username"
                    className="w-full mb-3 p-2 border rounded"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
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
                    className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
                >
                    Login
                </button>
            </form>
        </div>
    );
}

export default Login;