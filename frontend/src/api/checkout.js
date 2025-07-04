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