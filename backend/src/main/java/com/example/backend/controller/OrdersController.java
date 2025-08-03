package com.example.backend.controller;

import com.example.backend.model.OrderItemStatus;
import com.example.backend.service.OrdersPageService;
import com.example.backend.viewModel.OrderItemWithStatusViewModel;
import com.example.backend.viewModel.OrderViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    private final OrdersPageService ordersPageService;

    @Autowired
    public OrdersController(OrdersPageService ordersPageService) {
        this.ordersPageService = ordersPageService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderViewModel>>>  getOrdersPage(@RequestHeader("userId") int userId) {
        return ordersPageService.getOrdersByUserId(userId);
    }

    @PutMapping("/{orderItemId}/status")
    public ResponseEntity<ApiResponse<OrderItemWithStatusViewModel.StatusViewModel>> updateOrderItemStatus(
        @RequestHeader("userId") int userId,
        @PathVariable int orderItemId,
        @RequestBody Map<String, String> body) {

        String statusStr = body.get("status");
        OrderItemStatus.Status newStatus;
        try {
            newStatus = OrderItemStatus.Status.valueOf(statusStr);
        } catch (Exception e) {
            ApiResponse.ApiError error = new ApiResponse.ApiError("0", "Invalid status value", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, null, error));
        }

        return ordersPageService.updateOrderItemStatus(userId, orderItemId, newStatus);
    }
}