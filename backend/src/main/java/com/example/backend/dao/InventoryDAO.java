package com.example.backend.dao;

import com.example.backend.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryDAO extends JpaRepository<Inventory, Integer> {
    Optional<Inventory> findByProductId(int productId);
}
