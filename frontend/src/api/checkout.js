import axios from "axios";
import { API_BASE } from "./axios";
import {isNumberString} from "../utils/ApiValidator";

/**
 * Fetches the checkout page data for a specific user.
 * @param {number} userId The user's ID (required for the request header)
 * @returns {Promise} Promise resolving to CheckoutPageViewModel
 */
export async function getCheckoutDetailsByUserId(userId) {
    if (!isNumberString(userId)) {
        throw new Error("Invalid userId: must be a non-null, positive number string, instead:" + userId);
    }

    return axios.get(`${API_BASE}/checkout`, {
        headers: {
            userId: userId
        }
    });
}

/**
 * Places an order for a specific user.
 * @param {String} userId The user's ID (required for the request header)
 * @param {String} addressId The selected shipping address ID
 * @param {String} paymentId The selected payment method ID
 * @returns {Promise} Promise resolving to the created order
 */
export async function placeCartOrder(userId, addressId, paymentId) {
    if (!isNumberString(userId) || !isNumberString(addressId) || !isNumberString(paymentId)) {
        throw new Error("Invalid input: userId, addressId, and paymentId must be non-null, positive number strings.");
    }
    return axios.post(`${API_BASE}/checkout/cart`, {
        addressId,
        paymentId
    }, {
        headers: {
            userId: userId
        }
    });
}


/**
 * Places an order for a specific user.
 * @param {String} userId The user's ID (required for the request header)
 * @param {String} addressId The selected shipping address ID
 * @param {String} paymentId The selected payment method ID
 * @param {String} productId The selected shipping address ID
 * @param {String} paymentId The selected payment method ID
 * @returns {Promise} Promise resolving to the created order
 */
export async function placeBuyItNowOrder(userId, addressId, paymentId, productId, quantity) {
    if (!isNumberString(userId) || !isNumberString(addressId) || !isNumberString(paymentId)
        || !isNumberString(productId)) {
        throw new Error("Invalid input: userId, addressId, paymentId, productId and quantity must be non-null, positive number strings.");
    }
    if (typeof quantity !== "number" || quantity < 0) {
        throw new Error("Invalid quantity: must be a non-null, positive number.");
    }
    return axios.post(`${API_BASE}/checkout/buyitnow`, {
        addressId,
        paymentId,
        productId,
        quantity
    }, {
        headers: {
            userId: userId
        }
    });
}