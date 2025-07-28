package com.example.backend.controller;

import com.example.backend.model.Product;
import com.example.backend.service.DetailPageService;
import com.example.backend.service.HomeProductsService;
import com.example.backend.service.StoreService;
import com.example.backend.viewModel.DetailPageViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend.utils.ApiValidator;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final HomeProductsService homeProductsService;
    private final DetailPageService detailPageService;
    private final StoreService storeService;

    @Autowired
    public ProductController(HomeProductsService homeProductsService,
                             DetailPageService detailPageService,
                             StoreService storeService) {
        this.homeProductsService = homeProductsService;
        this.detailPageService = detailPageService;
        this.storeService = storeService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = homeProductsService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<DetailPageViewModel> getProductById(@PathVariable int productId) {
        DetailPageViewModel detail = detailPageService.getProductDetailById(productId);
        if (detail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detail);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable int categoryId) {
        List<Product> products = homeProductsService.getByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<DetailPageViewModel> addProduct(
            @RequestBody Map<String, String> productData,
            @RequestHeader("userId") String userIdStr) {

        return handleAddOrUpdateProduct(Optional.empty(), productData, userIdStr);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<DetailPageViewModel> updateProduct(
            @PathVariable int productId,
            @RequestBody Map<String, String> productData,
            @RequestHeader("userId") String userId) {

        return handleAddOrUpdateProduct(Optional.of(productId), productData, userId);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable int productId,
            @RequestHeader("userId") String userIdStr) {

        int userId;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        return storeService.deleteProduct(userId, productId);
    }

    private ResponseEntity<DetailPageViewModel> handleAddOrUpdateProduct(Optional<Integer> id, Map<String, String> productData, String userIdStr) {
        String name = productData.get("name");
        String priceStr = productData.get("price");

        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (priceStr == null || !ApiValidator.isNumeric(priceStr)) {
            return ResponseEntity.badRequest().build();
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        if (id.isPresent()) {
            DetailPageViewModel updated = storeService.updateStoreProduct(userId, id.get(), productData);
            return ResponseEntity.ok(updated);
        } else {
            DetailPageViewModel created = storeService.addStoreProduct(userId, productData);
            return ResponseEntity
                    .created(URI.create("/api/products/" + created.getId()))
                    .body(created);
        }
    }
}
