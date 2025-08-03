import axios from "axios";
import { API_BASE } from "./axios";
import { isNumberString, isPositiveInteger } from "../utils/ApiValidator";

export async function getProducts() {
    return axios.get(`${API_BASE}/products`, {});
}

export async function getProductDetailsById(productId) {
    return axios.get(`${API_BASE}/products/${productId}`, {});
}

export async function getProductsByCategory(categoryId) {
    return axios.get(`${API_BASE}/products/category/${categoryId}`, {});
}

function checkRequiredProductFields(data, isMultipart = false) {
    // Handles both plain objects and FormData
    const getField = (field) => {
        if (isMultipart && data instanceof FormData) {
            return data.get(field);
        }
        return data[field];
    };

    const requiredFields = ["name", "description", "price", "maxQuantity", "categoryName"];
    for (const field of requiredFields) {
        const value = getField(field);
        if (value === undefined || value === null || value === "" || (typeof value === "string" && value.trim() === "")) {
            throw new Error(`Missing or empty required product field: ${field}`);
        }
    }
    // Check price as number
    const price = getField("price");
    if (isNaN(Number(price))) {
        throw new Error("Price must be a number");
    }
    // Check maxQuantity as integer
    const maxQuantity = getField("maxQuantity");
    if (!Number.isInteger(Number(maxQuantity))) {
        throw new Error("Stock quantity (maxQuantity) must be an integer");
    }
}

/**
 * Add a new product to the store.
 * @param {string} userId
 * @param {object|FormData} productOrFormData
 * @param {boolean} isMultipart
 */
export async function addProduct(userId, productOrFormData, isMultipart = false) {
    if (!isNumberString(userId)) {
        throw new Error("Invalid userId: must be a non-null, positive number string, instead:" + userId);
    }
    checkRequiredProductFields(productOrFormData, isMultipart);

    return axios.post(
        `${API_BASE}/products`,
        productOrFormData,
        {
            headers: {
                userId,
                ...(isMultipart
                    ? { "Content-Type": "multipart/form-data" }
                    : { "Content-Type": "application/json" }),
            },
        }
    );
}

/**
 * Update an existing product in the store.
 * @param {string} userId
 * @param {number|string} productId
 * @param {object|FormData} productOrFormData
 * @param {boolean} isMultipart
 */
export async function updateProduct(userId,
                                    productId,
                                    productOrFormData,
                                    isMultipart = false) {
    if (!isNumberString(userId)) {
        throw new Error("Invalid userId: must be a non-null, positive number string, instead:" + userId);
    }
    if (!isPositiveInteger(productId)) {
        throw new Error("Invalid productId: must be a positive number, instead:" + productId);
    }
    checkRequiredProductFields(productOrFormData, isMultipart);

    return axios.put(
        `${API_BASE}/products/${productId}`,
        productOrFormData,
        {
            headers: {
                userId,
                ...(isMultipart
                    ? { "Content-Type": "multipart/form-data" }
                    : { "Content-Type": "application/json" }),
            },
        }
    );
}

/**
 * Delete a product from the store
 */
export async function deleteProduct(userId, productId) {
    if (!isNumberString(userId)) {
        throw new Error("Invalid userId: must be a non-null, positive number string, instead:" + userId);
    }
    if (!isPositiveInteger(productId)) {
        throw new Error("Invalid productId: must be a positive number, instead:" + productId);
    }
    return axios.delete(`${API_BASE}/products/${productId}`, {
        headers: { userId },
    });
}