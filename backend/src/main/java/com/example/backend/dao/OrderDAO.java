package com.example.backend.dao;

import com.example.backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDAO extends JpaRepository<Order, Integer> {
    /**
     * Finds all orders for a given user ID.
     * @param userId The ID of the user
     * @return List of Order objects for the user
     */
    List<Order> findByUserId(int userId);
}