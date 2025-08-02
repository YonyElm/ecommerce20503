package com.example.backend.service;

import com.example.backend.controller.ApiResponse;
import com.example.backend.dao.OrderDAO;
import com.example.backend.dao.OrderItemDAO;
import com.example.backend.dao.OrderItemStatusDAO;
import com.example.backend.dao.RoleDAO;
import com.example.backend.model.Order;
import com.example.backend.model.OrderItem;
import com.example.backend.model.OrderItemStatus;
import com.example.backend.model.Product;
import com.example.backend.viewModel.OrderItemWithStatusViewModel;
import com.example.backend.viewModel.OrderViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrdersPageService {

    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final OrderItemStatusDAO orderItemStatusDAO;
    private final RoleDAO roleDAO;

    @Autowired
    public OrdersPageService(OrderDAO orderDAO,
                             OrderItemDAO orderItemDAO,
                             OrderItemStatusDAO orderItemStatusDAO,
                             RoleDAO roleDAO) {
        this.orderDAO = orderDAO;
        this.orderItemDAO = orderItemDAO;
        this.orderItemStatusDAO = orderItemStatusDAO;
        this.roleDAO = roleDAO;
    }

    public ResponseEntity<ApiResponse<List<OrderViewModel>>> getOrdersByUserId(int userId) {

        ResponseEntity<ApiResponse<List<OrderViewModel>>> adminAccess = ApiResponse.checkAdminAccess(userId, "change order status", roleDAO);

        List<Order> orders = orderDAO.findByUserId(userId);
        if (orders == null || orders.isEmpty()) {
            return ApiResponse.errorResponse("0", "Ordered not found for user", HttpStatus.NOT_FOUND);
        }

        List<OrderViewModel> bodyForResponse = orders.stream().map(order -> {
            OrderViewModel viewModel = new OrderViewModel();

            Order proxyOrder = Order.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .build();
            viewModel.setOrder(proxyOrder);

            List<OrderItem> items = orderItemDAO.findByOrderId(order.getId());
            if (items == null) items = Collections.emptyList();

            List<OrderItemWithStatusViewModel> itemWithStatusList = items.stream().map(item -> {
                OrderItemWithStatusViewModel itemWithStatus = new OrderItemWithStatusViewModel();

                Product product = item.getProduct();
                Product proxyProduct = null;
                if (product != null) {
                    proxyProduct = Product.builder()
                        .name(product.getName())
                        .price(product.getPrice())
                        .id(product.getId())
                        .imageURL(product.getImageURL())
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
                    List<OrderItemStatus.Status> nextSteps =  determineNextSteps(lastStatus.getStatus(), (adminAccess==null));
                    OrderItemWithStatusViewModel.StatusViewModel proxyLastStatus = OrderItemWithStatusViewModel.StatusViewModel.builder()
                        .status(lastStatus.getStatus())
                        .updatedAt(lastStatus.getUpdatedAt())
                        .nextSteps(nextSteps)
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

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, bodyForResponse, null));
    }
    
    @Transactional
    public ResponseEntity<ApiResponse<OrderItemWithStatusViewModel.StatusViewModel>> updateOrderItemStatus(int adminUserId, int orderItemId, OrderItemStatus.Status newStatus) {
        ResponseEntity<ApiResponse<OrderItemStatus>> accessCheck = ApiResponse.checkAdminAccess(adminUserId, "update order item status", roleDAO);
        //TBD - not only admin!
        if (accessCheck != null) {
//            orderDAO.findByOredItem
            return ApiResponse.errorResponse("1", "Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        Optional<OrderItem> orderItemOpt = orderItemDAO.findById(orderItemId);
        if (orderItemOpt.isEmpty()) {
            return ApiResponse.errorResponse("2", "Order item not found", HttpStatus.NOT_FOUND);
        }

        OrderItemStatus status = OrderItemStatus.builder()
                .orderItem(orderItemOpt.get())
                .status(newStatus)
                .updatedAt(java.time.LocalDateTime.now())
                .build();
        OrderItemStatus savedStatus = orderItemStatusDAO.save(status);
        OrderItemWithStatusViewModel.StatusViewModel responseObject = OrderItemWithStatusViewModel.StatusViewModel.builder()
            .nextSteps(determineNextSteps(savedStatus.getStatus(), (accessCheck==null)))
            .status(savedStatus.getStatus())
            .updatedAt(savedStatus.getUpdatedAt())
            .build();

        ApiResponse<OrderItemWithStatusViewModel.StatusViewModel> response = new ApiResponse<>(true, responseObject, null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private List<OrderItemStatus.Status> determineNextSteps(OrderItemStatus.Status currentStatus, boolean isAdmin) {
        if (isAdmin) {
            // Admins can choose any status
            return List.of(OrderItemStatus.Status.values());
        }

        // Non-admins: limited flow
        return switch (currentStatus) {
            case processing, shipped -> List.of(OrderItemStatus.Status.cancelled);
            case delivered -> List.of(OrderItemStatus.Status.returned);
            default -> Collections.emptyList();
        };
    }

}
