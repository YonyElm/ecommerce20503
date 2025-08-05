package com.example.backend.controller;

import com.example.backend.model.OrderItemStatus;
import com.example.backend.service.OrdersPageService;
import com.example.backend.viewModel.OrderItemWithStatusViewModel;
import com.example.backend.viewModel.OrderViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class OrdersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrdersPageService ordersPageService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetOrdersPage() throws Exception {
        int userId = 1;
        boolean fetchAll = false;
        ResponseEntity<ApiResponse<List<OrderViewModel>>> responseEntity = ResponseEntity.ok(new ApiResponse<>(true, Collections.emptyList(), null));
        when(ordersPageService.getOrdersByUserId(userId, fetchAll)).thenReturn(responseEntity);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders")
                .header("userId", userId)
                .param("fetchAll", String.valueOf(fetchAll)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void testUpdateOrderItemStatus() throws Exception {
        int userId = 1;
        int orderItemId = 2;
        String status = "shipped";
        java.util.Map<String, String> body = java.util.Map.of("status", status);
        ResponseEntity<ApiResponse<OrderItemWithStatusViewModel.StatusViewModel>> responseEntity = ResponseEntity.ok(new ApiResponse<>(true, null, null));
        when(ordersPageService.updateOrderItemStatus(userId, orderItemId, OrderItemStatus.Status.valueOf(status))).thenReturn(responseEntity);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/orders/" + orderItemId + "/status")
                .header("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
