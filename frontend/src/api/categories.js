import axios from "axios";
import {API_BASE} from "./axios";

export async function getCategories() {
    return axios.get(`${API_BASE}/categories`, {});
}
