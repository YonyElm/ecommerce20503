package com.example.backend.controller;

import com.example.backend.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private UsersController usersController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsers() {
        int userId = 1;
        Map<String, Object> responseMap = new HashMap<>();
        ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(responseMap, HttpStatus.OK);
        when(adminService.getUsers(userId)).thenReturn(responseEntity);

        ResponseEntity<Map<String, Object>> result = usersController.getUsers(userId);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(responseMap, result.getBody());
        verify(adminService, times(1)).getUsers(userId);
    }

    @Test
    void testUpdateUserRole_ValidRequest() {
        int performerUserId = 1;
        Map<String, String> request = new HashMap<>();
        request.put("action", "true");
        request.put("targetUserId", "2");
        Map<String, Object> responseMap = new HashMap<>();
        ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(responseMap, HttpStatus.OK);
        when(adminService.userActivation(performerUserId, 2, true)).thenReturn(responseEntity);

        ResponseEntity<Map<String, Object>> result = usersController.updateUserRole(performerUserId, request);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(responseMap, result.getBody());
        verify(adminService, times(1)).userActivation(performerUserId, 2, true);
    }

    @Test
    void testUpdateUserRole_MissingFields() {
        int performerUserId = 1;
        Map<String, String> request = new HashMap<>();
        request.put("action", "true");
        // missing targetUserId
        ResponseEntity<Map<String, Object>> result = usersController.updateUserRole(performerUserId, request);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }
}
