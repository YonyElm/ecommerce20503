import axios from "axios";
import { API_BASE } from "./axios";
import { isNumberString } from "../utils/ApiValidator";

export async function getUserSettings(userId) {
  if (!isNumberString(userId)) {
    throw new Error(
      "Invalid userId: must be a non-null, positive number string, instead:" +
        userId
    );
  }

  return axios.get(`${API_BASE}/user-settings`, {
    headers: {
      userId: userId,
    },
  });
}

/**
 * Update user's personal info
 */
export async function updateUserProfile(userId, profile) {
  if (!isNumberString(userId)) {
    throw new Error(
      "Invalid userId: must be a non-null, positive number string, instead:" +
        userId
    );
  }
  return axios.put(`${API_BASE}/user-settings/profile/name`, profile, {
    headers: { userId },
  });
}

/**
 * Update user's Role
 */
export async function updateUserRole(userId, data) {
  if (!isNumberString(userId)) {
    throw new Error(
      "Invalid userId: must be a non-null, positive number string, instead:" +
      userId
    );
  }
  return axios.put(`${API_BASE}/user-settings/profile/role`, data, {
    headers: { userId },
  });
}

/**
 * Add a new address
 */
export async function addUserAddress(userId, address) {
  if (!isNumberString(userId)) {
    throw new Error(
        "Invalid userId: must be a non-null, positive number string, instead:" +
        userId
    );
  }
  return axios.post(`${API_BASE}/user-settings/addresses`, address, {
    headers: { userId },
  });
}

/**
 * Edit existing address
 */
export async function updateUserAddress(userId, addressId, address) {
  if (!isNumberString(userId)) {
    throw new Error(
        "Invalid userId: must be a non-null, positive number string, instead:" +
        userId
    );
  }
  return axios.put(`${API_BASE}/user-settings/addresses/${addressId}`, address, {
    headers: { userId },
  });
}

/**
 * Delete address
 */
export async function deleteUserAddress(userId, addressId) {
  if (!isNumberString(userId)) {
    throw new Error(
        "Invalid userId: must be a non-null, positive number string, instead:" +
        userId
    );
  }
  return axios.delete(`${API_BASE}/user-settings/addresses/${addressId}`, {
    headers: { userId },
  });
}

/**
 * Add payment method
 */
export async function addUserPaymentMethod(userId, payment)  {
  if (!isNumberString(userId)) {
    throw new Error(
      "Invalid userId: must be a non-null, positive number string, instead:" +
      userId
    );
  }
  return axios.post(`${API_BASE}/user-settings/payments`, payment, {
    headers: { userId },
  });
}

/**
 * Edit payment method
 */
export async function updateUserPaymentMethod(userId, paymentId, payment)  {
  if (!isNumberString(userId)) {
    throw new Error(
      "Invalid userId: must be a non-null, positive number string, instead:" +
      userId
    );
  }
  return axios.put(`${API_BASE}/user-settings/payments/${paymentId}`, payment, {
    headers: { userId },
  });
}

/**
 * Delete payment method
 */
export async function deleteUserPaymentMethod(userId, paymentId)  {
  if (!isNumberString(userId)) {
    throw new Error(
      "Invalid userId: must be a non-null, positive number string, instead:" +
      userId
    );
  }
  return axios.delete(`${API_BASE}/user-settings/payments/${paymentId}`, {
    headers: { userId },
  });
}
