import axios from "axios";

const API_BASE = process.env.REACT_APP_API_BASE_URL || "http://localhost:8080/api";

export async function login({ username, password }) {
    return axios.post(`${API_BASE}/auth/login`, { username, password });
}

export async function register({ username, email, password }) {
    return axios.post(`${API_BASE}/auth/register`, { username, password, email });
}