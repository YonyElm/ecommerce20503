import axios from "axios";
import {API_BASE} from "./axios";

export async function getCart(userId) {
    return axios.get(`${API_BASE}/cart`, {
        headers: {
            userId: userId
        }
    });
}

export async function updateCartItem(userId, productId, quantity) {
    const response = await axios.post(
        `${API_BASE}/cart/add`,
        null, // No request body needed, use null
        {
            headers: {
                userId: userId
            },
            params: {
                productId: productId,
                quantity: quantity
            }
        }
    );
    return response.data;
}

export async function removeCartItem(userId, productId) {
    return axios.delete(
        `${API_BASE}/cart/remove`,
        {
            headers: { userId: userId },
            params: { productId: productId }
        }
    );
}
