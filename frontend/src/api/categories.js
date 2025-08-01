import axios from "axios";
import {API_BASE} from "./axios";
import {isNumberString, isPositiveInteger} from "../utils/ApiValidator";

export async function getCategories() {
    return axios.get(`${API_BASE}/categories`, {});
}


/**
 * Create a category on the backend.
 *
 * @param {string} userId - User ID as a string representing a positive number
 * @param {Object} dataObject - Data object containing the fields to update,
 *                              expected body: { "categoryName": <categoryName> }
 * @returns {Promise} Axios promise for the HTTP POST request
 */
export async function createCategory(userId, dataObject) {
    if (!isNumberString(userId)) {
        throw new Error("Invalid userId: must be a non-null, positive number string, instead:" + userId);
    }
    let body = JSON.stringify(dataObject);
    return axios.post(`${API_BASE}/categories`, body, {
        headers: {
            userId,
            "Content-Type": "application/json"
        },
    });
}

/**
 * Update a category on the backend.
 *
 * @param {string} userId - User ID as a string representing a positive number
 * @param {number} categoryId - Category ID as a real number
 * @param {Object} dataObject - Data object containing the fields to update,
 *                              expected body: { "categoryName": <categoryName> }
 * @returns {Promise} Axios promise for the HTTP PUT request
 */
export async function updateCategory(userId, categoryId, dataObject) {
    if (!isNumberString(userId)) {
        throw new Error("Invalid userId: must be a non-null, positive number string, instead:" + userId);
    }
    if (!isPositiveInteger(categoryId)) {
        throw new Error("Invalid productId: must be a positive number, instead:" + categoryId);
    }
    let body = JSON.stringify(dataObject);
    return axios.put(`${API_BASE}/categories/${categoryId}`, body, {
        headers: {
            userId,
            "Content-Type": "application/json"
        },
    });
}

/**
 * Delete a category on the backend.
 *
 * @param {string} userId - User ID as a string representing a positive number
 * @param {number} categoryId - Category ID as a real number
 * @returns {Promise} Axios promise for the HTTP Delete request
 */
export async function deleteCategory(userId, categoryId) {
    if (!isNumberString(userId)) {
        throw new Error("Invalid userId: must be a non-null, positive number string, instead:" + userId);
    }
    if (!isPositiveInteger(categoryId)) {
        throw new Error("Invalid productId: must be a positive number, instead:" + categoryId);
    }
    return axios.delete(`${API_BASE}/categories/${categoryId}`, {
        headers: {
            userId,
            "Content-Type": "application/json"
        },
    });
}