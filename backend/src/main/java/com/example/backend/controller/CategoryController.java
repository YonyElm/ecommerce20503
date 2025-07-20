package com.example.backend.controller;

import com.example.backend.model.Category;
import com.example.backend.dao.CategoryDAO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryDAO categoryDAO;

    @Autowired
    public CategoryController(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @GetMapping
    @Transactional
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryDAO.findAll());
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<Category> getCategoryById(@PathVariable int id) {
        Optional<Category> categoryOptional = categoryDAO.findById(id);
        if (categoryOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Category category = categoryOptional.get();
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> createCategory(@Valid @RequestBody Category category, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        categoryDAO.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}