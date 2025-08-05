package com.example.backend.controller;

import com.example.backend.model.Address;
import com.example.backend.model.Payment;
import com.example.backend.service.UserSettingsPageService;
import com.example.backend.viewModel.UserSettingsPageViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class UserSettingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserSettingsPageService userSettingsService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetUserSettings() throws Exception {
        int userId = 1;
        ResponseEntity<ApiResponse<UserSettingsPageViewModel>> responseEntity =
                ResponseEntity.ok(new ApiResponse<>(true, null, null));
        when(userSettingsService.getUserSettings(userId)).thenReturn(responseEntity);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user-settings")
                .header("userId", userId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void testUpdateUserProfile() throws Exception {
        int userId = 1;
        Map<String, String> request = Map.of("fullName", "Test User");
        org.mockito.Mockito.doNothing().when(userSettingsService).updateUserProfile(userId, "Test User");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user-settings/profile/name")
                .header("userId", userId)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void testUpdateUserRole() throws Exception {
        int performerUserId = 1;
        Map<String, String> request = Map.of("roleName", "ADMIN", "targetUserId", "2");
        ResponseEntity<Map<String, String>> responseEntity = ResponseEntity.ok(Map.of("roleName", "ADMIN"));
        when(userSettingsService.updateUserRole(performerUserId, 2, "ADMIN")).thenReturn(responseEntity);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user-settings/profile/role")
                .header("userId", performerUserId)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void testAddAddress() throws Exception {
        int userId = 1;
        Map<String, Object> request = new HashMap<>();
        request.put("fullName", "Test User");
        request.put("addressLine1", "123 Main St");
        request.put("city", "City");
        request.put("postalCode", "12345");
        request.put("country", "Country");
        Address address = new Address();
        when(userSettingsService.addOrUpdateAddress(ArgumentMatchers.eq(userId), ArgumentMatchers.isNull(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.any())).thenReturn(address);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user-settings/addresses")
                .header("userId", userId)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }


    @Test
    void testUpdateAddress() throws Exception {
        int userId = 1;
        int addressId = 2;
        Map<String, Object> request = new HashMap<>();
        request.put("fullName", "Test User");
        request.put("addressLine1", "123 Main St");
        request.put("city", "City");
        request.put("postalCode", "12345");
        request.put("country", "Country");
        Address address = new Address();
        when(userSettingsService.addOrUpdateAddress(ArgumentMatchers.eq(userId), ArgumentMatchers.eq(addressId),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.any())).thenReturn(address);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user-settings/addresses/" + addressId)
                .header("userId", userId)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void testDeleteAddress() throws Exception {
        int userId = 1;
        int addressId = 2;
        org.mockito.Mockito.doNothing().when(userSettingsService).deleteAddress(userId, addressId);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user-settings/addresses/" + addressId)
                .header("userId", userId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void testAddPaymentMethod() throws Exception {
        int userId = 1;
        Map<String, Object> request = Map.of("name", "VISA");
        Payment payment = new Payment();
        when(userSettingsService.addOrUpdatePaymentMethod(ArgumentMatchers.eq(userId), ArgumentMatchers.isNull(), ArgumentMatchers.anyString())).thenReturn(payment);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user-settings/payments")
                .header("userId", userId)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
