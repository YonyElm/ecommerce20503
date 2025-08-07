package com.example.backend.dao;

import com.example.backend.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemDAO extends JpaRepository<OrderItem, Integer> {
    /**
     * Finds all order items for a given order ID.
     * @param orderId The ID of the order
     * @return List of OrderItem objects for the order
     */
    List<OrderItem> findByOrderId(int orderId);

    /**
     * Finds an order item by its ID.
     * @param orderItemId The ID of the order item
     * @return Optional containing the OrderItem if found, or empty otherwise
     */
    Optional<OrderItem> findById(int orderItemId);
}