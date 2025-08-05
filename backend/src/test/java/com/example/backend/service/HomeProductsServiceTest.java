package com.example.backend.service;

import com.example.backend.dao.InventoryDAO;
import com.example.backend.dao.ProductDAO;
import com.example.backend.model.Inventory;
import com.example.backend.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HomeProductsServiceTest {
    @Mock
    private ProductDAO productDAO;
    @Mock
    private InventoryDAO inventoryDAO;
    @InjectMocks
    private HomeProductsService homeProductsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        Product product = new Product();
        product.setId(1);
        when(productDAO.findByIsActiveTrue()).thenReturn(List.of(product));
        Inventory inventory = new Inventory();
        inventory.setQuantity(10);
        when(inventoryDAO.findByProductId(1)).thenReturn(Optional.of(inventory));
        List<Product> result = homeProductsService.getAllProducts();
        assertEquals(1, result.size());
        assertEquals(product, result.get(0));
    }

    @Test
    void testGetByCategory() {
        Product product = new Product();
        product.setId(2);
        when(productDAO.findByCategory_IdAndIsActiveTrue(5)).thenReturn(List.of(product));
        Inventory inventory = new Inventory();
        inventory.setQuantity(5);
        when(inventoryDAO.findByProductId(2)).thenReturn(Optional.of(inventory));
        List<Product> result = homeProductsService.getByCategory(5);
        assertEquals(1, result.size());
        assertEquals(product, result.get(0));
    }
}
