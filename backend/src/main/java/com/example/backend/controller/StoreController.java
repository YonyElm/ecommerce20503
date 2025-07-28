package com.example.backend.controller;

import com.example.backend.service.StoreService;
import com.example.backend.viewModel.DetailPageViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/store")
public class StoreController {

    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getStore(@RequestHeader("userId") int userId) {
        Map<String, Object> products = storeService.getStore(userId);
        if (products == null) {
            return ResponseEntity.notFound().build(); // user not found or not seller/admin
        }
        return ResponseEntity.ok(products);
    }

}
