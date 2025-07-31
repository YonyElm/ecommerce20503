import axios from "axios";
import {API_BASE} from "./axios";
import {isNumberString} from "../utils/ApiValidator";

/**
 * request to fetch all users signed up to the service
 */
export async function getUsers(userId) {
  if (!isNumberString(userId)) {
    throw new Error("Invalid userId: must be a non-null, positive number string, instead:" + userId);
  }
  return axios.get(`${API_BASE}/users`, {
    headers: { userId },
  });
}

/**
 * Used to activate/deactivate a user
 */
export async function activateUser(userId, data) {
  if (!isNumberString(userId)) {
    throw new Error(
      "Invalid userId: must be a non-null, positive number string, instead:" +
      userId
    );
  }
  return axios.put(`${API_BASE}/users/activate`, data, {
    headers: { userId },
  });
}
