import { useState } from "react";
import { register } from "../api/auth";
import { useNavigate, Link } from "react-router-dom";

function Register() {
    const [form, setForm] = useState({ username: "", password: "", email: ""});
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await register(form);
            navigate("/login");
        } catch (err) {
            setError("Registration failed");
        }
    };

    return (
        <div className="max-w-md mx-auto mt-20 p-6 shadow-xl rounded-xl bg-white">
            <h2 className="text-2xl font-bold mb-4">Register</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    name="username"
                    placeholder="Username"
                    className="w-full mb-3 p-2 border rounded"
                    value={form.username}
                    onChange={handleChange}
                    required
                />
                <input
                    type="password"
                    name="password"
                    placeholder="Password"
                    className="w-full mb-3 p-2 border rounded"
                    value={form.password}
                    onChange={handleChange}
                    required
                />
                <input
                    type="email"
                    name="email"
                    placeholder="Email"
                    className="w-full mb-3 p-2 border rounded"
                    value={form.email}
                    onChange={handleChange}
                    required
                />
                {error && <p className="text-red-600 mb-2">{error}</p>}
                <button
                    type="submit"
                    className="w-full bg-cyan-500 text-white py-2 rounded hover:bg-cyan-700"
                >
                    Sign Up
                </button>
            </form>
            <div className="mt-4 text-center">
                <span className="text-gray-700">Already have an account?</span>{" "}
                <Link to="/login" className="text-teal-700 hover:text-cyan-500 font-medium">
                    Sign in
                </Link>
            </div>
        </div>
    );
}

export default Register;