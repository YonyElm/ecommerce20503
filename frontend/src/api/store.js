import axios from "axios";
import { API_BASE } from "./axios";
import {isNumberString} from "../utils/ApiValidator";

/**
 * Get all products for a specific seller/store owner
 */
export async function getStore(userId) {
    if (!isNumberString(userId)) {
        throw new Error("Invalid userId: must be a non-null, positive number string, instead:" + userId);
    }
    return axios.get(`${API_BASE}/store`, {
        headers: { userId },
    });
}