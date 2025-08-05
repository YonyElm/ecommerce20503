package com.example.backend.controller;

import com.example.backend.dao.ShoppingCartDAO;
import com.example.backend.model.ShoppingCart;
import com.example.backend.service.CartPageService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingCartDAO cartDAO;

    @MockBean
    private CartPageService cartPageService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetCart() throws Exception {
        int userId = 1;
        // Mock the service to return a list
        when(cartPageService.getCartItemsByUserId(userId)).thenReturn(List.of());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cart")
                .header("userId", userId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void testAddItem() throws Exception {
        int userId = 1;
        int productId = 2;
        int quantity = 3;
        ShoppingCart cart = new ShoppingCart();
        when(cartDAO.getOrCreateCart(userId)).thenReturn(cart);
        // No return for addOrUpdateItem (void)
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/add")
                .header("userId", userId)
                .param("productId", String.valueOf(productId))
                .param("quantity", String.valueOf(quantity)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Item added to cart")));
    }


    @Test
    void testRemoveItem() throws Exception {
        int cartItemId = 5;
        // No return for removeItem (void)
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cart/remove/" + cartItemId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
