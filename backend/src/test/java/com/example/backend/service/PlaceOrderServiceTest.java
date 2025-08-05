package com.example.backend.service;

import com.example.backend.dao.*;
import com.example.backend.model.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PlaceOrderServiceTest {
    @Mock
    private OrderDAO orderDAO;
    @Mock
    private OrderItemDAO orderItemDAO;
    @Mock
    private OrderItemStatusDAO orderItemStatusDAO;
    @Mock
    private InventoryDAO inventoryDAO;
    @Mock
    private ShoppingCartDAO shoppingCartDAO;
    @Mock
    private CartItemDAO cartItemDAO;
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private PlaceOrderService placeOrderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPlaceCartOrder_EmptyCart() {
        int userId = 1, addressId = 2, paymentId = 3;
        ShoppingCart cart = new ShoppingCart();
        when(shoppingCartDAO.getOrCreateCart(userId)).thenReturn(cart);
        when(cartItemDAO.findByCartAndIsActiveTrue(cart)).thenReturn(List.of());
        assertThrows(RuntimeException.class, () -> placeOrderService.placeCartOrder(userId, addressId, paymentId));
    }

    @Test
    void testPlaceBuyItNowOrder_InventoryNotFound() {
        int userId = 1, addressId = 2, paymentId = 3, productId = 4, quantity = 1;
        Product product = new Product();
        product.setId(productId);
        when(entityManager.getReference(Product.class, productId)).thenReturn(product);
        when(inventoryDAO.findByProductId(productId)).thenReturn(Optional.empty());
        when(entityManager.getReference(User.class, userId)).thenReturn(new User());
        when(entityManager.getReference(Address.class, addressId)).thenReturn(new Address());
        when(entityManager.getReference(Payment.class, paymentId)).thenReturn(new Payment());
        assertThrows(RuntimeException.class, () -> placeOrderService.placeBuyItNowOrder(userId, addressId, paymentId, productId, quantity));
    }
}
