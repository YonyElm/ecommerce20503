package com.example.backend.controller;

import com.example.backend.model.Product;
import com.example.backend.service.DetailPageService;
import com.example.backend.service.HomeProductsService;
import com.example.backend.service.StoreService;
import com.example.backend.viewModel.DetailPageViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    // Add Product (multipart support)
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<DetailPageViewModel> addProductMultipart(
            @RequestParam Map<String, String> productData,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestHeader("userId") String userIdStr) {
        return handleAddOrUpdateProduct(Optional.empty(), productData, userIdStr, image);
    }

    // Add Product (JSON support, fallback)
    @PostMapping(consumes = "application/json")
    public ResponseEntity<DetailPageViewModel> addProductJson(
            @RequestBody Map<String, String> productData,
            @RequestHeader("userId") String userIdStr) {

        return handleAddOrUpdateProduct(Optional.empty(), productData, userIdStr, null);
    }

    // Update Product (multipart support)
    @PutMapping(path="/{productId}", consumes = {"multipart/form-data"})
    public ResponseEntity<DetailPageViewModel> updateProductMultipart(
            @PathVariable int productId,
            @RequestParam Map<String, String> productData,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestHeader("userId") String userId) {
        return handleAddOrUpdateProduct(Optional.of(productId), productData, userId, image);
    }

    // Update Product (JSON support, fallback)
    @PutMapping(path="/{productId}", consumes = "application/json")
    public ResponseEntity<DetailPageViewModel> updateProductJson(
            @PathVariable int productId,
            @RequestBody Map<String, String> productData,
            @RequestHeader("userId") String userId) {
        return handleAddOrUpdateProduct(Optional.of(productId), productData, userId, null);
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

    private ResponseEntity<DetailPageViewModel> handleAddOrUpdateProduct(
            Optional<Integer> productId, Map<String, String> productData, String userIdStr, MultipartFile image) {
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

        if (productId.isPresent()) {
            DetailPageViewModel updated = storeService.updateStoreProduct(userId, productId.get(), productData, image);
            return ResponseEntity.status(HttpStatus.OK).body(updated);
        } else {
            DetailPageViewModel created = storeService.addStoreProduct(userId, productData, image);
            return ResponseEntity
                    .created(URI.create("/api/products/" + created.getId()))
                    .body(created);
        }
    }
}