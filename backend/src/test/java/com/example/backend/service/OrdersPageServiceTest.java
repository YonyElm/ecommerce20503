package com.example.backend.service;

import com.example.backend.controller.ApiResponse;
import com.example.backend.dao.OrderDAO;
import com.example.backend.dao.OrderItemDAO;
import com.example.backend.dao.OrderItemStatusDAO;
import com.example.backend.dao.RoleDAO;
import com.example.backend.model.*;
import com.example.backend.viewModel.OrderItemWithStatusViewModel;
import com.example.backend.viewModel.OrderViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OrdersPageServiceTest {
    @Mock
    private OrderDAO orderDAO;
    @Mock
    private OrderItemDAO orderItemDAO;
    @Mock
    private OrderItemStatusDAO orderItemStatusDAO;
    @Mock
    private RoleDAO roleDAO;
    @InjectMocks
    private OrdersPageService ordersPageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOrdersByUserId_NotAdmin() {
        int userId = 1;
        Order order = new Order();
        order.setId(1);
        when(roleDAO.getUserRoles(userId)).thenReturn(List.of());
        when(orderDAO.findByUserId(userId)).thenReturn(List.of(order));
        when(orderItemDAO.findByOrderId(order.getId())).thenReturn(Collections.emptyList());
        ResponseEntity<ApiResponse<List<OrderViewModel>>> response = ordersPageService.getOrdersByUserId(userId, false);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testUpdateOrderItemStatus_OrderItemNotFound() {
        when(orderItemDAO.findById(anyInt())).thenReturn(Optional.empty());
        ResponseEntity<ApiResponse<OrderItemWithStatusViewModel.StatusViewModel>> response = ordersPageService.updateOrderItemStatus(1, 2, OrderItemStatus.Status.processing);
        assertEquals(404, response.getStatusCodeValue());
        assertFalse(response.getBody().isSuccess());
    }
}
