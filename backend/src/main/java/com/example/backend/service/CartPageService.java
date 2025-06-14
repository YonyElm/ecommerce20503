package com.example.backend.service;

import com.example.backend.dao.CartItemDAO;
import com.example.backend.dao.ShoppingCartDAO;
import com.example.backend.model.*;
import com.example.backend.viewModel.CartPageItemViewModel;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartPageService {

    private final CartItemDAO cartItemDAO;
    private final ShoppingCartDAO cartDAO;

    @Autowired
    public CartPageService(CartItemDAO cartItemDAO,
                           ShoppingCartDAO cartDAO) {
        this.cartItemDAO = cartItemDAO;
        this.cartDAO = cartDAO;
    }

    public List<CartPageItemViewModel> getCartItemsByUserId(int userId) {

        List<CartPageItemViewModel> cartPageItems = new ArrayList<>();
        ShoppingCart cart = cartDAO.getOrCreateCart(userId);
        cart.setItems(cartItemDAO.findByCart(cart));

        for (CartItem item : cart.getItems()) {
            CartPageItemViewModel itemModel = new CartPageItemViewModel();
            Product product = item.getProduct();
            itemModel.setId(product.getId());
            itemModel.setName(product.getName());
            itemModel.setPrice(product.getPrice());
            itemModel.setQuantity(item.getQuantity());

            cartPageItems.add(itemModel);
        }

        return cartPageItems;
    }

    @Transactional
    public void addOrUpdateItem(ShoppingCart cart, int productId, int quantity) {
        Optional<CartItem> existing = cartItemDAO.findByCart(cart).stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst();

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(quantity);
            cartItemDAO.save(item);
        } else {
            Product proxyProduct = new Product();
            proxyProduct.setId(productId);
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(proxyProduct);
            item.setQuantity(quantity);
            cartItemDAO.save(item);
        }
    }

    @Transactional
    public void removeItem(ShoppingCart cart, int productId) {
        cartItemDAO.deleteByCartAndProductId(cart, productId);
    }

}
