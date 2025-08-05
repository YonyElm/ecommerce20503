package com.example.backend.controller;

import com.example.backend.dao.RoleDAO;
import com.example.backend.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiResponseTest {

    @Test
    void testCheckAdminAccess_UserNotFound() {
        RoleDAO roleDAO = mock(RoleDAO.class);
        when(roleDAO.getUserRoles(1)).thenReturn(null);
        ResponseEntity<ApiResponse<Object>> response = ApiResponse.checkAdminAccess(1, "test action", roleDAO);
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ApiResponse<Object> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertNotNull(body.getError());
        assertEquals("1", body.getError().getCode());
    }

    @Test
    void testCheckAdminAccess_UserNotAdmin() {
        RoleDAO roleDAO = mock(RoleDAO.class);
        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.CUSTOMER); // Use CUSTOMER instead of USER
        when(roleDAO.getUserRoles(1)).thenReturn(List.of(userRole));
        ResponseEntity<ApiResponse<Object>> response = ApiResponse.checkAdminAccess(1, "test action", roleDAO);
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ApiResponse<Object> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertNotNull(body.getError());
        assertEquals("2", body.getError().getCode());
    }

    @Test
    void testCheckAdminAccess_UserIsAdmin() {
        RoleDAO roleDAO = mock(RoleDAO.class);
        Role adminRole = new Role();
        adminRole.setRoleName(Role.RoleName.ADMIN);
        when(roleDAO.getUserRoles(1)).thenReturn(List.of(adminRole));
        ResponseEntity<ApiResponse<Object>> response = ApiResponse.checkAdminAccess(1, "test action", roleDAO);
        assertNull(response); // null means access granted
    }

    @Test
    void testErrorResponse() {
        ResponseEntity<ApiResponse<Object>> response = ApiResponse.errorResponse("404", "Not found", HttpStatus.NOT_FOUND);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ApiResponse<Object> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertNotNull(body.getError());
        assertEquals("404", body.getError().getCode());
        assertEquals("Not found", body.getError().getMessage());
    }
}
