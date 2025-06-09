package com.example.backend.dao;

import com.example.backend.model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class InventoryDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public InventoryDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper for Inventory
    private static final RowMapper<Inventory> inventoryRowMapper = new RowMapper<>() {
        @Override
        public Inventory mapRow(ResultSet rs, int rowNum) throws SQLException {
            Inventory inventory = new Inventory();
            inventory.setProductId(rs.getInt("product_id"));
            inventory.setQuantity(rs.getInt("quantity"));
            inventory.setLastUpdated(rs.getTimestamp("last_updated"));
            return inventory;
        }
    };

    public Inventory findByProductId(int productId) {
        String sql = "SELECT * FROM inventory WHERE product_id = ?";
        return jdbcTemplate.query(sql, inventoryRowMapper, productId)
                .stream().findFirst().orElse(null);
    }

    public int insertInventory(Inventory inventory) {
        String sql = "INSERT INTO inventory (product_id, quantity, last_updated) VALUES (?, ?, CURRENT_TIMESTAMP)";
        return jdbcTemplate.update(sql,
                inventory.getProductId(),
                inventory.getQuantity());
    }

    public int updateInventory(Inventory inventory) {
        String sql = "UPDATE inventory SET quantity = ?, last_updated = CURRENT_TIMESTAMP WHERE product_id = ?";
        return jdbcTemplate.update(sql,
                inventory.getQuantity(),
                inventory.getProductId());
    }
}
