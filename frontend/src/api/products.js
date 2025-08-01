import axios from "axios";
import {API_BASE} from "./axios";
import {isNumberString, isPositiveInteger} from "../utils/ApiValidator";

export async function getProducts() {
    return axios.get(`${API_BASE}/products`, {});
}

export async function getProductDetailsById(productId) {
    return axios.get(`${API_BASE}/products/${productId}`, {});
}

export async function getProductsByCategory(categoryId) {
    return axios.get(`${API_BASE}/products/category/${categoryId}`, {});
}

/**
 * Add a new product to the store
 */
export async function addProduct(userId, product) {
    if (!isNumberString(userId)) {
        throw new Error("Invalid userId: must be a non-null, positive number string, instead:" + userId);
    }
    return axios.post(`${API_BASE}/products`, product, {
        headers: { userId },
        "Content-Type": "application/json"
    });
}

/**
 * Update an existing product in the store
 */
export async function updateProduct(userId, productId, product) {
    if (!isNumberString(userId)) {
        throw new Error("Invalid userId: must be a non-null, positive number string, instead:" + userId);
    }
    if (!isPositiveInteger(productId)) {
        throw new Error("Invalid productId: must be a positive number, instead:" + productId);
    }
    return axios.put(`${API_BASE}/products/${productId}`, product, {
        headers: {
            userId,
            "Content-Type": "application/json"
        },
    });
}


/**
 * Delete a product from the store
 */
export async function deleteProduct(userId, productId) {
    if (!isNumberString(userId)) {
        throw new Error("Invalid userId: must be a non-null, positive number string, instead:" + userId);
    }
    return axios.delete(`${API_BASE}/products/${productId}`, {
        headers: { userId },
    });
}