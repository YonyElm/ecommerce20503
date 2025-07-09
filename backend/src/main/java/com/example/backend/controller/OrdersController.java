package com.example.backend.controller;

import com.example.backend.service.OrdersPageService;
import com.example.backend.viewModel.OrderViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    private final OrdersPageService ordersPageService;

    @Autowired
    public OrdersController(OrdersPageService ordersPageService) {
        this.ordersPageService = ordersPageService;
    }

    @GetMapping
    public ResponseEntity<List<OrderViewModel>> getOrdersPage(@RequestHeader("userId") int userId) {
        return ResponseEntity.status(HttpStatus.OK).body(ordersPageService.getOrdersByUserId(userId));
    }
}