import axios from "axios";
import { API_BASE } from "./axios";
import {isNumberString} from "../utils/ApiValidator";

export async function getOrdersPage(userId, dataObject) {
    if (!isNumberString(userId)) {
        throw new Error("Invalid userId: must be a non-null, positive number string, instead:" + userId);
    }

    return axios.get(`${API_BASE}/orders`, {
        headers: {
            userId: userId,
            "Content-Type": "application/json"
        },
        params: {
            fetchAll: dataObject.fetchAll
        }
    });
}

export async function updateOrderItemStatus(userId, orderItemId, dataObject) {
    if (!isNumberString(userId)) {
        throw new Error("Invalid userId: must be a non-null, positive number string, instead:" + userId);
    }
    let body = JSON.stringify(dataObject);
    return axios.put(`${API_BASE}/orders/${orderItemId}/status`, body, {
        headers: {
            userId: userId,
            "Content-Type": "application/json"
        }
    });
}