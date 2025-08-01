package com.example.backend.controller;

import com.example.backend.model.Category;
import com.example.backend.service.AdminService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final AdminService adminService;

    @Autowired
    public CategoryController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getAllCategories());
    }

    // expected body: {"categoryName": <categoryName>}
    @PostMapping
    public ResponseEntity<ApiResponse<Category>> createCategory(@RequestHeader("userId") int userId,
                                                                @RequestBody Map<String, String> body) {

        String categoryName = body.get("categoryName");
        ResponseEntity<ApiResponse<Category>> response = categoryNameChecker(categoryName);
        if (response != null) return response;

        return adminService.createCategory(userId, categoryName);
    }

    // expected body: {"categoryName": <categoryName>}
    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(@RequestHeader("userId") int userId,
                                            @PathVariable("categoryId") int categoryId,
                                            @RequestBody Map<String, String> body) {

        String categoryName = body.get("categoryName");
        ResponseEntity<ApiResponse<Category>> response = categoryNameChecker(categoryName);
        if (response != null) return response;

        return adminService.updateCategory(userId, categoryId, categoryName);
    }


    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@RequestHeader("userId") int userId,
                                            @PathVariable("categoryId") int categoryId) {
        return adminService.deleteCategory(userId, categoryId);
    }

    private ResponseEntity<ApiResponse<Category>> categoryNameChecker(@RequestBody String categoryName) {
        if (categoryName == null || categoryName.isEmpty()) {
            ApiResponse.ApiError errorResponse = new ApiResponse.ApiError("0", "Category name cannot be null or empty", null);
            ApiResponse<Category> response = new ApiResponse<>(false, null, errorResponse);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return null;
    }
}
