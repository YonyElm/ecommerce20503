package com.example.backend.service;

import com.example.backend.dao.OrderDAO;
import com.example.backend.dao.OrderItemDAO;
import com.example.backend.dao.OrderItemStatusDAO;
import com.example.backend.model.Order;
import com.example.backend.model.OrderItem;
import com.example.backend.model.OrderItemStatus;
import com.example.backend.model.Product;
import com.example.backend.viewModel.OrderItemWithStatus;
import com.example.backend.viewModel.OrderViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersPageService {

    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final OrderItemStatusDAO orderItemStatusDAO;

    @Autowired
    public OrdersPageService(OrderDAO orderDAO,
                             OrderItemDAO orderItemDAO,
                             OrderItemStatusDAO orderItemStatusDAO) {
        this.orderDAO = orderDAO;
        this.orderItemDAO = orderItemDAO;
        this.orderItemStatusDAO = orderItemStatusDAO;
    }

    public List<OrderViewModel> getOrdersByUserId(int userId) {
        List<Order> orders = orderDAO.findByUserId(userId);
        if (orders == null || orders.isEmpty()) {
            return Collections.emptyList();
        }

        return orders.stream().map(order -> {
            OrderViewModel viewModel = new OrderViewModel();

            Order proxyOrder = Order.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .build();
            viewModel.setOrder(proxyOrder);

            List<OrderItem> items = orderItemDAO.findByOrderId(order.getId());
            if (items == null) items = Collections.emptyList();

            List<OrderItemWithStatus> itemWithStatusList = items.stream().map(item -> {
                OrderItemWithStatus itemWithStatus = new OrderItemWithStatus();

                Product product = item.getProduct();
                Product proxyProduct = null;
                if (product != null) {
                    proxyProduct = Product.builder()
                        .name(product.getName())
                        .price(product.getPrice())
                        .id(product.getId())
                        .build();
                }

                OrderItem proxyOrderItem = OrderItem.builder()
                    .id(item.getId())
                    .pricePerUnit(item.getPricePerUnit())
                    .quantity(item.getQuantity())
                    .product(proxyProduct)
                    .build();
                itemWithStatus.setOrderItem(proxyOrderItem);

                OrderItemStatus lastStatus = orderItemStatusDAO.findTopByOrderItemIdOrderByUpdatedAtDesc(item.getId());
                if (lastStatus != null) {
                    OrderItemStatus proxyLastStatus = OrderItemStatus.builder()
                        .status(lastStatus.getStatus())
                        .updatedAt(lastStatus.getUpdatedAt())
                        .build();
                    itemWithStatus.setStatusList(Collections.singletonList(proxyLastStatus));
                } else {
                    itemWithStatus.setStatusList(Collections.emptyList());
                }

                return itemWithStatus;
            }).collect(Collectors.toList());

            viewModel.setOrderItemList(itemWithStatusList);

            // Currently not used by frontend
            // viewModel.setShippingAddress(order.getAddress());
            // viewModel.setPaymentMethod(order.getPayment());

            return viewModel;
        }).collect(Collectors.toList());
    }
}
