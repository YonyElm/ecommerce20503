package com.example.backend.dao;

import com.example.backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryDAO extends JpaRepository<Category, Integer> {
    @NonNull
    List<Category> findAll();
    Optional<Category> findById(int id);
    Optional<Category> findFirstByNameIs(String categoryName);
}
