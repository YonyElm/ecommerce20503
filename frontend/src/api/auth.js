import axios from "axios";
import {API_BASE} from "./axios";

export async function login({ username, password }) {
    return axios.post(`${API_BASE}/auth/login`, { username, password });
}

export async function register({ username, email, password }) {
    return axios.post(`${API_BASE}/auth/register`, { username, password, email });
}