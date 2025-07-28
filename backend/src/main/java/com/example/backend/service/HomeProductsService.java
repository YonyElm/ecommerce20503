package com.example.backend.service;

import com.example.backend.dao.InventoryDAO;
import com.example.backend.dao.ProductDAO;
import com.example.backend.model.Inventory;
import com.example.backend.model.Product;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class HomeProductsService {

    private final ProductDAO productDAO;
    private final InventoryDAO inventoryDAO;
    private static final Logger logger = LoggerFactory.getLogger(HomeProductsService.class);

    @Autowired
    public HomeProductsService(ProductDAO productDAO, InventoryDAO inventoryDAO) {
        this.productDAO = productDAO;
        this.inventoryDAO = inventoryDAO;
    }

    @Transactional
    public List<Product> getAllProducts() {
        return filterInventoryStatus(productDAO.findByIsActiveTrue());
    }

    @Transactional
    public List<Product> getByCategory(int categoryId) {
        return filterInventoryStatus(productDAO.findByCategory_IdAndIsActiveTrue(categoryId));
    }

    private List<Product> filterInventoryStatus(List<Product> products) {
        return products.stream().map(product -> {
            try {
                Inventory inventory = inventoryDAO.findByProductId(product.getId()).orElse(null);
                if (inventory == null) {
                    logger.error("Inventory for productId={} is null", product.getId());
                    return null;
                }
                return product;
            } catch (Exception e) {
                logger.error("Exception while fetching inventory for productId={}: {}", product.getId(), e.getMessage());
                return null; // Skip this product
            }
        })
                .filter(Objects::nonNull)
                .toList();
    }
}
