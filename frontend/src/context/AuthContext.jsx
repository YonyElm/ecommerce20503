import { createContext, useState, useEffect } from "react";
import { jwtDecode, jwtVerify} from 'jwt-decode';

export const AuthContext = createContext();

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (token) {
            try {
                // const verify = jwtVerify(token, process.env.REACT_APP_JWT_SECRET)
                const decoded = jwtDecode(token);
                setUser(decoded);
            } catch (err) {
                console.error("Invalid token");
            }
        }
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
        <AuthContext.Provider value={{ user, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}