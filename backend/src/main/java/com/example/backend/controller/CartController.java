package com.example.backend.controller;

import com.example.backend.dao.ShoppingCartDAO;
import com.example.backend.model.ShoppingCart;
import com.example.backend.service.CartPageService;
import com.example.backend.viewModel.CartPageItemViewModel;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class CartController {

    private final ShoppingCartDAO cartDAO;
    private final CartPageService cartPageService;

    @GetMapping
    public ResponseEntity<List<CartPageItemViewModel>> getCart(@RequestHeader("userId") int userId) {
        return ResponseEntity.status(HttpStatus.OK).body(cartPageService.getCartItemsByUserId(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addItem(@RequestHeader("userId") int userId,
                                          @RequestParam int productId,
                                          @RequestParam int quantity) {
        ShoppingCart cart = cartDAO.getOrCreateCart(userId);
        cartPageService.addOrUpdateItem(cart, productId, quantity);
        return ResponseEntity.ok("Item added to cart");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeItem(@RequestHeader("userId") int userId,
                                             @RequestParam int productId) {
        ShoppingCart cart = cartDAO.getOrCreateCart(userId);
        cartPageService.removeItem(cart, productId);
        return ResponseEntity.ok("Item removed from cart");
    }
}
