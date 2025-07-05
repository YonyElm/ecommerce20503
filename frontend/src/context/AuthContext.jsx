import { createContext, useState, useEffect } from "react";
import { jwtDecode, jwtVerify} from 'jwt-decode';

export const AuthContext = createContext();

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true); // <-- add this

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (token) {
            try {
                // const verify = jwtVerify(token, process.env.REACT_APP_JWT_SECRET)
                const decoded = jwtDecode(token);
                setUser(decoded);
            } catch (err) {
                console.error("Invalid token, " + err);
                setUser(null);
            }
        }
        setLoading(false); // <-- set as NOT loading after check (in all cases)
    }, []);

    const login = (token) => {
        localStorage.setItem("token", token);
        const decoded = jwtDecode(token);
        setUser(decoded);
    };

    const logout = () => {
        localStorage.removeItem("token");
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, login, logout, loading }}>
            {children}
        </AuthContext.Provider>
    );
}