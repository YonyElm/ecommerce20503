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

    /**
     * Constructor for RoleDAO.
     * @param jdbcTemplate JdbcTemplate for database operations
     */
    @Autowired
    public RoleDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retrieves the list of roles assigned to a user.
     * @param userId The ID of the user
     * @return List of Role objects for the user
     */
    public List<Role> getUserRoles(int userId) {
        String sql = """
            SELECT r.id, r.role_name
            FROM roles r
            JOIN user_roles ur ON r.id = ur.role_id
            WHERE ur.user_id = ?
        """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Role.class), userId);
    }

    /**
     * Assigns a role to a user in the user_roles table.
     * @param userId The ID of the user
     * @param roleId The ID of the role to assign
     */
    public void assignRoleToUser(int userId, int roleId) {
        String sql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, roleId);
    }

    /**
     * Finds a role by its name.
     * @param name The name of the role
     * @return The Role object matching the name
     */
    public Role findByName(String name) {
        String sql = "SELECT * FROM roles WHERE role_name = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Role.class), name);
    }
}
