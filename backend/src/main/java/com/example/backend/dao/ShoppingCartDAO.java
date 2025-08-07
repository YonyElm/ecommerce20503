package com.example.backend.dao;

import com.example.backend.model.ShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ShoppingCartDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Retrieves the shopping cart for a user, or creates one if it does not exist.
     * @param userId The ID of the user
     * @return The ShoppingCart object for the user
     */
    public ShoppingCart getOrCreateCart(int userId) {
        List<ShoppingCart> carts = jdbcTemplate.query(
                "SELECT * FROM shopping_cart WHERE user_id = ?",
                new BeanPropertyRowMapper<>(ShoppingCart.class), userId
        );

        if (!carts.isEmpty()) {
            return carts.get(0);
        }

        jdbcTemplate.update("INSERT INTO shopping_cart (user_id) VALUES (?)", userId);
        return getOrCreateCart(userId); // Try again to retrieve the created cart
    }
}
