package com.example.backend.service;

import com.example.backend.dao.CartItemDAO;
import com.example.backend.dao.ShoppingCartDAO;
import com.example.backend.model.CartItem;
import com.example.backend.model.Product;
import com.example.backend.model.ShoppingCart;
import com.example.backend.viewModel.CartPageItemViewModel;
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

class CartPageServiceTest {
    @Mock
    private CartItemDAO cartItemDAO;
    @Mock
    private ShoppingCartDAO cartDAO;
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private CartPageService cartPageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCartItemsByUserId() {
        int userId = 1;
        ShoppingCart cart = new ShoppingCart();
        CartItem item = new CartItem();
        Product product = new Product();
        product.setId(2);
        product.setName("Test");
        item.setProduct(product);
        item.setId(3);
        item.setQuantity(5);
        cart.setItems(List.of(item));
        when(cartDAO.getOrCreateCart(userId)).thenReturn(cart);
        when(cartItemDAO.findByCartAndIsActiveTrue(cart)).thenReturn(List.of(item));
        List<CartPageItemViewModel> result = cartPageService.getCartItemsByUserId(userId);
        assertEquals(1, result.size());
        assertEquals(product.getId(), result.get(0).getProductId());
    }

    @Test
    void testAddOrUpdateItem_NewItem() {
        ShoppingCart cart = new ShoppingCart();
        int productId = 2;
        int quantity = 3;
        when(cartItemDAO.findByCartAndIsActiveTrue(cart)).thenReturn(List.of());
        Product product = new Product();
        product.setId(productId);
        when(entityManager.getReference(Product.class, productId)).thenReturn(product);
        cartPageService.addOrUpdateItem(cart, productId, quantity);
        verify(cartItemDAO, times(1)).save(any(CartItem.class));
    }

    @Test
    void testAddOrUpdateItem_ExistingItem() {
        ShoppingCart cart = new ShoppingCart();
        int productId = 2;
        int quantity = 3;
        Product product = new Product();
        product.setId(productId);
        CartItem item = new CartItem();
        item.setProduct(product);
        when(cartItemDAO.findByCartAndIsActiveTrue(cart)).thenReturn(List.of(item));
        cartPageService.addOrUpdateItem(cart, productId, quantity);
        verify(cartItemDAO, times(1)).save(item);
    }

    @Test
    void testRemoveItem() {
        int cartItemId = 5;
        cartPageService.removeItem(cartItemId);
        verify(cartItemDAO, times(1)).deactivateByCartItemId(cartItemId);
    }
}
