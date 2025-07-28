package com.example.backend.dao;

import com.example.backend.model.Product;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDAO extends JpaRepository<Product, Integer> {
    @NonNull
    List<Product> findByIsActiveTrue();
    Optional<Product> findByIdAndIsActiveTrue(int productId);
    List<Product> findByCategory_IdAndIsActiveTrue(int categoryId);
    List<Product> findBySeller_IdAndIsActiveTrue(int sellerId);
}
