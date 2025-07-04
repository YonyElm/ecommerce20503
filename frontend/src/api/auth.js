import axios from "axios";
import {API_BASE} from "./axios";

export async function login({ email, password }) {
    return axios.post(`${API_BASE}/auth/login`, { email, password });
}

export async function register({ email, password, fullName }) {
    return axios.post(`${API_BASE}/auth/register`, { email, password, fullName });
}