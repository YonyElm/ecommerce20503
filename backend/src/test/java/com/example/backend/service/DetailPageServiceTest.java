package com.example.backend.service;

import com.example.backend.dao.InventoryDAO;
import com.example.backend.dao.ProductDAO;
import com.example.backend.model.Category;
import com.example.backend.model.Inventory;
import com.example.backend.model.Product;
import com.example.backend.viewModel.DetailPageViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DetailPageServiceTest {
    @Mock
    private ProductDAO productDAO;
    @Mock
    private InventoryDAO inventoryDAO;
    @InjectMocks
    private DetailPageService detailPageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProductDetailById_Found() {
        int id = 1;
        Product product = new Product();
        product.setId(id);
        product.setName("Test");
        product.setDescription("desc");
        product.setPrice(BigDecimal.TEN);
        Category category = new Category();
        category.setName("Books");
        product.setCategory(category);
        product.setImageURL("img.png");
        Inventory inventory = new Inventory();
        inventory.setQuantity(5);
        when(productDAO.findByIdAndIsActiveTrue(id)).thenReturn(Optional.of(product));
        when(inventoryDAO.findByProductId(id)).thenReturn(Optional.of(inventory));
        DetailPageViewModel vm = detailPageService.getProductDetailById(id);
        assertNotNull(vm);
        assertEquals("Test", vm.getName());
        assertEquals("Books", vm.getCategoryName());
        assertEquals(5, vm.getMaxQuantity());
    }

    @Test
    void testGetProductDetailById_NotFound() {
        when(productDAO.findByIdAndIsActiveTrue(1)).thenReturn(Optional.empty());
        assertNull(detailPageService.getProductDetailById(1));
    }
}
