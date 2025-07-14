package com.example.backend.dao;

import com.example.backend.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class RoleDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RoleDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Role> getUserRoles(int userId) {
        String sql = """
            SELECT r.id, r.role_name
            FROM roles r
            JOIN user_roles ur ON r.id = ur.role_id
            WHERE ur.user_id = ?
        """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Role.class), userId);
    }

    public void assignRoleToUser(int userId, int roleId) {
        String sql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, roleId);
    }

    public Role findByName(String name) {
        String sql = "SELECT * FROM roles WHERE role_name = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Role.class), name);
    }
}
