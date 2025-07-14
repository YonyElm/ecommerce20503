package com.example.backend.dao;

import com.example.backend.model.CartItem;
import com.example.backend.model.ShoppingCart;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemDAO extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByCartAndIsActiveTrue(ShoppingCart cart);

    @Modifying
    @Transactional
    @Query("UPDATE CartItem c SET c.isActive = false WHERE c.id = :cartItemId")
    void deactivateByCartItemId(@Param("cartItemId") int cartItemId);
}
