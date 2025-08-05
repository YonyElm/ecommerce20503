package com.example.backend.service;

import com.example.backend.controller.ApiResponse;
import com.example.backend.dao.CategoryDAO;
import com.example.backend.dao.RoleDAO;
import com.example.backend.dao.UserDAO;
import com.example.backend.model.Category;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {
    @Mock
    private UserDAO userDAO;
    @Mock
    private RoleDAO roleDAO;
    @Mock
    private CategoryDAO categoryDAO;
    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsers_AdminUser() {
        int userId = 1;
        User user = new User();
        user.setId(userId);
        when(userDAO.findById(userId)).thenReturn(Optional.of(user));
        Role adminRole = new Role();
        adminRole.setRoleName(Role.RoleName.ADMIN);
        when(roleDAO.getUserRoles(userId)).thenReturn(List.of(adminRole));
        when(userDAO.findAll()).thenReturn(List.of(user));
        when(roleDAO.getUserRoles(user.getId())).thenReturn(List.of(adminRole));

        ResponseEntity<Map<String, Object>> response = adminService.getUsers(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("users"));
    }

    @Test
    void testGetUsers_NonAdminUser() {
        int userId = 2;
        User user = new User();
        user.setId(userId);
        when(userDAO.findById(userId)).thenReturn(Optional.of(user));
        Role customerRole = new Role();
        customerRole.setRoleName(Role.RoleName.CUSTOMER);
        when(roleDAO.getUserRoles(userId)).thenReturn(List.of(customerRole));

        ResponseEntity<Map<String, Object>> response = adminService.getUsers(userId);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("message"));
    }

    @Test
    void testUserActivation_PerformerNotFound() {
        int performerId = 1, targetId = 2;
        when(roleDAO.getUserRoles(performerId)).thenReturn(Collections.emptyList());
        ResponseEntity<Map<String, Object>> response = adminService.userActivation(performerId, targetId, true);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("message"));
    }

    @Test
    void testCreateCategory_Admin() {
        int userId = 1;
        String categoryName = "Books";
        Role adminRole = new Role();
        adminRole.setRoleName(Role.RoleName.ADMIN);
        when(roleDAO.getUserRoles(userId)).thenReturn(List.of(adminRole));
        when(categoryDAO.save(any())).thenReturn(new Category());
        when(roleDAO.getUserRoles(anyInt())).thenReturn(List.of(adminRole));
        ResponseEntity<ApiResponse<Category>> response = adminService.createCategory(userId, categoryName);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ApiResponse<Category> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.isSuccess());
    }

    @Test
    void testGetAllCategories() {
        when(categoryDAO.findAll()).thenReturn(List.of(new Category()));
        List<Category> result = adminService.getAllCategories();
        assertFalse(result.isEmpty());
    }
}
