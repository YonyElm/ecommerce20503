package com.example.backend.dao;

import com.example.backend.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class CategoryDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CategoryDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Category> getAllCategories() {
        return jdbcTemplate.query("SELECT * FROM categories", new BeanPropertyRowMapper<>(Category.class))
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Category getCategoryById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM categories WHERE id = ?", new BeanPropertyRowMapper<>(Category.class), id);
    }

    public void addCategory(Category category) {
        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";
        jdbcTemplate.update(sql, category.getName(), category.getDescription());
    }
}
