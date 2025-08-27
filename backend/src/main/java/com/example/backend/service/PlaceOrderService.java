package com.example.backend.service;

import com.example.backend.dao.*;
import com.example.backend.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlaceOrderService {
    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final OrderItemStatusDAO orderItemStatusDAO;
    private final InventoryDAO inventoryDAO;
    private final ShoppingCartDAO shoppingCartDAO;
    private final CartItemDAO cartItemDAO;

    @PersistenceContext
    private EntityManager entityManager;


    /**
     * Constructor for PlaceOrderService.
     * @param orderDAO DAO for order data access
     * @param orderItemDAO DAO for order item data access
     * @param orderItemStatusDAO DAO for order item status data access
     * @param inventoryDAO DAO for inventory data access
     * @param shoppingCartDAO DAO for shopping cart data access
     * @param cartItemDAO DAO for cart item data access
     */
    @Autowired
    public PlaceOrderService(OrderDAO orderDAO, OrderItemDAO orderItemDAO, OrderItemStatusDAO orderItemStatusDAO, InventoryDAO inventoryDAO, ShoppingCartDAO shoppingCartDAO, CartItemDAO cartItemDAO) {
        this.orderDAO = orderDAO;
        this.orderItemDAO = orderItemDAO;
        this.orderItemStatusDAO = orderItemStatusDAO;
        this.inventoryDAO = inventoryDAO;
        this.shoppingCartDAO = shoppingCartDAO;
        this.cartItemDAO = cartItemDAO;
    }


    /**
     * Places an order for all items in the user's cart, updates inventory, and sets order status.
     * @param userId The ID of the user placing the order
     * @param addressId The ID of the shipping address
     * @param paymentId The ID of the payment method
     */
    @Transactional
    public void placeCartOrder(int userId,int addressId, int paymentId) {
        User user = entityManager.getReference(User.class, userId);
        Address address = entityManager.getReference(Address.class, addressId);
        Payment payment = entityManager.getReference(Payment.class, paymentId);
        ShoppingCart cart = shoppingCartDAO.getOrCreateCart(userId);
        List<CartItem> cartItems = cartItemDAO.findByCartAndIsActiveTrue(cart);
        if (cartItems.isEmpty()) throw new RuntimeException("Cart is empty");

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setPayment(payment);
        order.setTotalAmount(cartItems.stream().mapToDouble(item -> item.getProduct().getPrice().doubleValue() * item.getQuantity()).sum());
        order = orderDAO.save(order);

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            int productId = product != null ? product.getId() : -1;
            if (product == null || !product.getIsActive()) {
                throw new RuntimeException("Product is not longer available: " + productId);
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPricePerUnit(cartItem.getProduct().getPrice().doubleValue());
            orderItem = orderItemDAO.save(orderItem);

            Inventory inventory = inventoryDAO.findByProductId(cartItem.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Inventory not found for product id: " + productId));
            if (inventory == null || inventory.getQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Not enough inventory for product " + productId);
            } else {
                inventory.setQuantity(inventory.getQuantity() - cartItem.getQuantity());
                inventoryDAO.save(inventory);
            }

            OrderItemStatus status = new OrderItemStatus();
            status.setOrderItem(orderItem);
            orderItemStatusDAO.save(status);
        }

        for (CartItem cartItem : cartItems) {
            cartItemDAO.deactivateByCartItemId(cartItem.getId());
        }
    }


    /**
     * Places a "Buy It Now" order for a single product, updates inventory, and sets order status.
     * @param userId The ID of the user placing the order
     * @param addressId The ID of the shipping address
     * @param paymentId The ID of the payment method
     * @param productId The ID of the product to buy
     * @param quantity The quantity to purchase
     */
    @Transactional
    public void placeBuyItNowOrder(int userId, int addressId, int paymentId, int productId, int quantity) {
        User user = entityManager.getReference(User.class, userId);
        Address address = entityManager.getReference(Address.class, addressId);
        Payment payment = entityManager.getReference(Payment.class, paymentId);
        Product product = entityManager.getReference(Product.class, productId);

        // Check inventory
        Inventory inventory = inventoryDAO.findByProductId(product.getId())
                .orElseThrow(() -> new RuntimeException("Inventory not found for product id: " + product.getId()));
        if (inventory == null || inventory.getQuantity() < quantity) {
            throw new RuntimeException("Not enough inventory for product " + productId);
        } else {
            inventory.setQuantity(inventory.getQuantity() - quantity);
            inventoryDAO.save(inventory);
        }

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setPayment(payment);
        order.setTotalAmount(product.getPrice().doubleValue() * quantity);
        order = orderDAO.save(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setPricePerUnit(product.getPrice().doubleValue());
        orderItem = orderItemDAO.save(orderItem);

        // Add initial status
        OrderItemStatus status = new OrderItemStatus();
        status.setOrderItem(orderItem);
        orderItemStatusDAO.save(status);
    }
}
