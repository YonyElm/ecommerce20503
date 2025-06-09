import axios from "axios";
import {API_BASE} from "./axios";

export async function getProducts() {
    return axios.get(`${API_BASE}/products`, {});
}

export async function getProductDetailsById(id) {
    return axios.get(`${API_BASE}/products/${id}`, {});
}
