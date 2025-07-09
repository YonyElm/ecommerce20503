package com.example.backend.dao;

import com.example.backend.model.OrderItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemStatusDAO extends JpaRepository<OrderItemStatus, Long> {
    List<OrderItemStatus> findByOrderItemId(int orderItemId);
    // Retrieve the most recently updated status for a given order item
    OrderItemStatus findTopByOrderItemIdOrderByUpdatedAtDesc(int orderItemId);
}