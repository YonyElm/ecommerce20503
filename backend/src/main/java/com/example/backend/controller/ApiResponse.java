package com.example.backend.controller;

import com.example.backend.dao.RoleDAO;
import com.example.backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private ApiError error;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiError {
        private String code;
        private String message;
        private Map<String, Object> details;
    }

    public static <T> ResponseEntity<ApiResponse<T>> checkAdminAccess(int userId, String actionDescription, RoleDAO roleDAO) {
        List<Role> roles = roleDAO.getUserRoles(userId);
        if (roles == null || roles.isEmpty()) {
            return errorResponse("1", "Performing user not found with id: " + userId, HttpStatus.UNAUTHORIZED);
        }

        boolean isAdmin = roles.stream()
                .map(Role::getRoleName)
                .anyMatch(role -> role == Role.RoleName.ADMIN);

        if (!isAdmin) {
            return errorResponse("2", "User is not allowed to " + actionDescription, HttpStatus.UNAUTHORIZED);
        }

        return null; // null means access granted to Admin
    }

    public static <T> ResponseEntity<ApiResponse<T>> errorResponse(String code, String message, HttpStatus status) {
        ApiResponse.ApiError error = new ApiResponse.ApiError(code, message, null);
        ApiResponse<T> response = new ApiResponse<>(false, null, error);
        return ResponseEntity.status(status).body(response);
    }
}
