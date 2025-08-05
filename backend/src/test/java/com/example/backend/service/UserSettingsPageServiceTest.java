package com.example.backend.service;

import com.example.backend.controller.ApiResponse;
import com.example.backend.dao.AddressDAO;
import com.example.backend.dao.PaymentDAO;
import com.example.backend.dao.RoleDAO;
import com.example.backend.dao.UserDAO;
import com.example.backend.model.*;
import com.example.backend.viewModel.UserSettingsPageViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserSettingsPageServiceTest {
    @Mock
    private UserDAO userDAO;
    @Mock
    private RoleDAO roleDAO;
    @Mock
    private AddressDAO addressDAO;
    @Mock
    private PaymentDAO paymentDAO;
    @InjectMocks
    private UserSettingsPageService userSettingsPageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserSettings_UserNotFound() {
        when(userDAO.findByIdAndIsActiveTrue(1)).thenReturn(Optional.empty());
        ResponseEntity<ApiResponse<UserSettingsPageViewModel>> response = userSettingsPageService.getUserSettings(1);
        assertEquals(404, response.getStatusCode().value());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    void testUpdateUserProfile() {
        when(userDAO.updateFullNameById(1, "Test")).thenReturn(0);
        userSettingsPageService.updateUserProfile(1, "Test");
        verify(userDAO, times(1)).updateFullNameById(1, "Test");
    }

    @Test
    void testDeleteAddress_NotFound() {
        when(addressDAO.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userSettingsPageService.deleteAddress(1, 1));
    }

    @Test
    void testDeletePaymentMethod_NotFound() {
        when(paymentDAO.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userSettingsPageService.deletePaymentMethod(1, 1));
    }
}
