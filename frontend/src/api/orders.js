import axios from "axios";
import { API_BASE } from "./axios";
import {isNumberString} from "../utils/ApiValidator";

export async function getOrdersPage(userId) {
    if (!isNumberString(userId)) {
        throw new Error("Invalid userId: must be a non-null, positive number string, instead:" + userId);
    }

    return axios.get(`${API_BASE}/orders`, {
        headers: {
            userId: userId
        }
    });
}