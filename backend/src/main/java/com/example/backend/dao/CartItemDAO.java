package com.example.backend.dao;

import com.example.backend.model.CartItem;
import com.example.backend.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemDAO extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByCart(ShoppingCart cart);
    void deleteByCartAndProductId(ShoppingCart cart, int productId);
}
