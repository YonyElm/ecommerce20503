package com.example.backend.dao;

import com.example.backend.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentDAO extends JpaRepository<Payment, Integer> {
    List<Payment> findByUserIdAndIsActive(int UserId, Boolean isActive);
}