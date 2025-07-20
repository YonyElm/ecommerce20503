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
    List<Product> findAll();
    Optional<Product> findById(int productId);
    List<Product> findByCategory_Id(int categoryId);
}
