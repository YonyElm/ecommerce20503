package com.example.backend.dao;

import com.example.backend.model.Category;
import com.example.backend.model.Product;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class ProductDAO {

    private final JdbcTemplate jdbcTemplate;
    private CategoryDAO categoryDAO;

    @Autowired
    public ProductDAO(@NotNull JdbcTemplate jdbcTemplate,@NotNull CategoryDAO categoryDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.categoryDAO = categoryDAO;
    }

    private RowMapper<Product> productRowMapper = (rs, rowNum) -> {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        BigDecimal price = rs.getBigDecimal("price");
        int categoryId = rs.getInt("category_id");
        Category category = categoryDAO.getCategoryById(categoryId);
        Timestamp createdAt = rs.getTimestamp("created_at");
        return new Product(id, name, description, price, category, createdAt);
    };

    // Get all products
    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, productRowMapper).stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // Get a product by ID
    public Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, productRowMapper);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Add a new product
    public Product addProduct(Product product) {
        String sql = "INSERT INTO products (name, description, price, category_id) VALUES (?, ?, ?, ?) RETURNING id";
        Integer id = jdbcTemplate.queryForObject(sql, new Object[]{
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory().getId() // Use the ID from the Category object
        }, Integer.class);

        product.setId(id);
        return product;
    }

    public List<Product> getProductsByCategory(int categoryId) {
        return jdbcTemplate.query("SELECT * FROM products WHERE category_id = ?", productRowMapper, categoryId)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}