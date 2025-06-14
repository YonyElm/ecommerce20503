package com.example.backend.controller;

import com.example.backend.model.Product;
import com.example.backend.dao.ProductDAO;
import com.example.backend.service.DetailPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductDAO productDAO;
    private final DetailPageService detailPageService;

    @Autowired
    public ProductController(ProductDAO productDAO, DetailPageService detailPageService) {
        this.productDAO = productDAO;
        this.detailPageService = detailPageService;
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productDAO.getAllProducts());
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build(); // Return 400 Bad Request if validation fails
        }
        Product createdProduct = productDAO.addProduct(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(detailPageService.getProductDetailById(id));
    }

    @GetMapping("/category/{categoryId}")
    public List<Product> getByCategory(@PathVariable int categoryId) {
        return productDAO.getProductsByCategory(categoryId);
    }
}
