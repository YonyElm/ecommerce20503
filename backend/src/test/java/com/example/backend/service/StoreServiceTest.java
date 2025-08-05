package com.example.backend.service;

import com.example.backend.dao.*;
import com.example.backend.model.*;
import com.example.backend.viewModel.DetailPageViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StoreServiceTest {
    @Mock
    private ProductDAO productDAO;
    @Mock
    private UserDAO userDAO;
    @Mock
    private RoleDAO roleDAO;
    @Mock
    private InventoryDAO inventoryDAO;
    @Mock
    private CategoryDAO categoryDAO;
    @Mock
    private ResourceLoader resourceLoader;
    @InjectMocks
    private StoreService storeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetStore_UserNotFound() {
        when(userDAO.findById(1)).thenReturn(Optional.empty());
        Map<String, Object> result = storeService.getStore(1);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetStore_Admin() {
        int userId = 1;
        User user = new User();
        user.setId(userId);
        when(userDAO.findById(userId)).thenReturn(Optional.of(user));
        Role adminRole = new Role();
        adminRole.setRoleName(Role.RoleName.ADMIN);
        when(roleDAO.getUserRoles(userId)).thenReturn(List.of(adminRole));
        when(productDAO.findByIsActiveTrue()).thenReturn(List.of(new Product()));
        when(categoryDAO.findAll()).thenReturn(List.of(new Category()));
        Map<String, Object> result = storeService.getStore(userId);
        assertTrue(result.containsKey("products"));
        assertTrue(result.containsKey("categories"));
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productDAO.findByIdAndIsActiveTrue(1)).thenReturn(Optional.empty());
        ResponseEntity<Void> response = storeService.deleteProduct(1, 1);
        assertEquals(404, response.getStatusCode().value());
    }
}
