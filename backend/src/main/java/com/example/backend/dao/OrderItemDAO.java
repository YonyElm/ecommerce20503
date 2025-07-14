package com.example.backend.dao;

import com.example.backend.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemDAO extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(int orderId);
}