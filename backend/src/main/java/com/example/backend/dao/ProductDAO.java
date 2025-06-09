package com.example.backend.dao;

import com.example.backend.model.Category;
import com.example.backend.model.Product;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class ProductDAO {

    private final JdbcTemplate jdbcTemplate;
    private final CategoryDAO categoryDAO;
    private final RowMapper<Product> productRowMapper;

    @Autowired
    public ProductDAO(@NotNull JdbcTemplate jdbcTemplate,@NotNull CategoryDAO categoryDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.categoryDAO = categoryDAO;
        this.productRowMapper = createProductRowMapper();
    }

    private RowMapper<Product> createProductRowMapper() {
        return (rs, rowNum) -> {
            int categoryId = rs.getInt("category_id");
            Category category = null;

            if (!rs.wasNull()) { // Only attempt to fetch category if the column wasn't NULL
                category = categoryDAO.getCategoryById(categoryId);
            }

            return new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getBigDecimal("price"),
                category,
                rs.getTimestamp("created_at")
            );
        };
    }

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
        return jdbcTemplate.query(sql, productRowMapper, id)
                .stream()
                .findFirst()
                .orElse(null);
    }

    // Add a new product
    public Product addProduct(Product product) {
        String sql = "INSERT INTO products (name, description, price, category_id) VALUES (?, ?, ?, ?) RETURNING id";
        Integer id = jdbcTemplate.queryForObject(sql, new Object[]{
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory().getId()
        }, Integer.class);

        product.setId(id);
        return product;
    }

    // Get products by category
    public List<Product> getProductsByCategory(int categoryId) {
        String sql = "SELECT * FROM products WHERE category_id = ?";
        return jdbcTemplate.query(sql, productRowMapper, categoryId).stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
