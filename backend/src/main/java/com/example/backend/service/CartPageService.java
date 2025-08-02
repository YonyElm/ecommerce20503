package com.example.backend.service;

import com.example.backend.dao.CartItemDAO;
import com.example.backend.dao.ShoppingCartDAO;
import com.example.backend.model.*;
import com.example.backend.viewModel.CartPageItemViewModel;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartPageService {

    private final CartItemDAO cartItemDAO;
    private final ShoppingCartDAO cartDAO;
    private final EntityManager entityManager;

    @Autowired
    public CartPageService(CartItemDAO cartItemDAO,
                           ShoppingCartDAO cartDAO,
                           EntityManager entityManager) {
        this.cartItemDAO = cartItemDAO;
        this.cartDAO = cartDAO;
        this.entityManager = entityManager;
    }

    public List<CartPageItemViewModel> getCartItemsByUserId(int userId) {

        List<CartPageItemViewModel> cartPageItems = new ArrayList<>();
        ShoppingCart cart = cartDAO.getOrCreateCart(userId);
        cart.setItems(cartItemDAO.findByCartAndIsActiveTrue(cart));

        for (CartItem item : cart.getItems()) {
            CartPageItemViewModel itemModel = new CartPageItemViewModel();
            Product product = item.getProduct();
            itemModel.setItemId(item.getId());
            itemModel.setProductId(product.getId());
            itemModel.setName(product.getName());
            itemModel.setPrice(product.getPrice());
            itemModel.setQuantity(item.getQuantity());
            itemModel.setImageURL(product.getImageURL());

            cartPageItems.add(itemModel);
        }

        return cartPageItems;
    }

    @Transactional
    public void addOrUpdateItem(ShoppingCart cart, int productId, int quantity) {
        Optional<CartItem> existing = cartItemDAO.findByCartAndIsActiveTrue(cart).stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst();

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(quantity);
            cartItemDAO.save(item);
        } else {
            Product proxyProduct = entityManager.getReference(Product.class, productId);
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(proxyProduct);
            item.setQuantity(quantity);
            item.setIsActive(true);
            cartItemDAO.save(item);
        }
    }

    @Transactional
    public void removeItem(int cartItemId) {
        cartItemDAO.deactivateByCartItemId(cartItemId);
    }

}
