package com.example.backend.service;

import com.example.backend.controller.ApiResponse;
import com.example.backend.dao.OrderDAO;
import com.example.backend.dao.OrderItemDAO;
import com.example.backend.dao.OrderItemStatusDAO;
import com.example.backend.dao.RoleDAO;
import com.example.backend.model.*;
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


    /**
     * Constructor for OrdersPageService.
     * @param orderDAO DAO for order data access
     * @param orderItemDAO DAO for order item data access
     * @param orderItemStatusDAO DAO for order item status data access
     * @param roleDAO DAO for role data access
     */
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


    /**
     * Retrieves orders for a user, or all orders if the user is an admin and fetchAll is true.
     * @param userId The ID of the user requesting orders
     * @param fetchAll Whether to fetch all orders (admin only)
     * @return ResponseEntity containing a list of OrderViewModel or an error response
     */
    public ResponseEntity<ApiResponse<List<OrderViewModel>>> getOrdersByUserId(int userId, boolean fetchAll) {

        ResponseEntity<ApiResponse<List<OrderViewModel>>> adminAccess = ApiResponse.checkAdminAccess(userId, "change order status", roleDAO);

        // Not Admin but asked to fetch all
        if (adminAccess != null && fetchAll) {
            return ApiResponse.errorResponse("0", "Unauthorized action by UserId" + userId, HttpStatus.UNAUTHORIZED);
        }
        List<Order> orders;
        if (adminAccess == null && fetchAll) {
            orders = orderDAO.findAll();
        } else {
            orders = orderDAO.findByUserId(userId);
        }
        if (orders == null || orders.isEmpty()) {
            return ApiResponse.errorResponse("1", "Ordered not found for user", HttpStatus.NOT_FOUND);
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
    

    /**
     * Updates the status of an order item, if the user is authorized and the status transition is valid.
     * @param userId The ID of the user performing the update
     * @param orderItemId The ID of the order item to update
     * @param newStatus The new status to set
     * @return ResponseEntity containing the updated status view model or an error response
     */
    @Transactional
    public ResponseEntity<ApiResponse<OrderItemWithStatusViewModel.StatusViewModel>> updateOrderItemStatus(int userId, int orderItemId, OrderItemStatus.Status newStatus) {
        ResponseEntity<ApiResponse<OrderItemStatus>> accessCheck = ApiResponse.checkAdminAccess(userId, "update order item status", roleDAO);

        Optional<OrderItem> optionalOrderItem =  orderItemDAO.findById(orderItemId);
        if (optionalOrderItem.isEmpty()) {
            return ApiResponse.errorResponse("1", "Cant find orderItemId:" + orderItemId, HttpStatus.NOT_FOUND);
        }
        OrderItem orderItem = optionalOrderItem.get();

        // When not an Admin, make sure target resource belongs to performing user
        if (accessCheck != null) {
            Order relevantOrder = orderItem.getOrder();
            User relevantUser = relevantOrder.getUser();
            if (relevantUser.getId() != userId) {
                return ApiResponse.errorResponse("2", "User is not authorized to perform action on:" + orderItemId, HttpStatus.UNAUTHORIZED);
            }
        }

        // Check newStatus is valid
        OrderItemStatus lastStatus = orderItemStatusDAO.findTopByOrderItemIdOrderByUpdatedAtDesc(orderItem.getId());
        List<OrderItemStatus.Status> expectedNextSteps = determineNextSteps(lastStatus.getStatus(), (accessCheck==null));
        if (!expectedNextSteps.contains(newStatus) || lastStatus.getStatus().equals(newStatus)) {
            return ApiResponse.errorResponse("3", "Cant move status from:" + lastStatus.getStatus() + " to "
                    + newStatus, HttpStatus.BAD_REQUEST);
        }

        // Perform action
        OrderItemStatus status = OrderItemStatus.builder()
                .orderItem(orderItem)
                .status(newStatus)
                .updatedAt(java.time.LocalDateTime.now())
                .build();
        OrderItemStatus savedStatus = orderItemStatusDAO.save(status);

        // Return ViewModel to Frontend
        OrderItemWithStatusViewModel.StatusViewModel responseObject = OrderItemWithStatusViewModel.StatusViewModel.builder()
            .nextSteps(determineNextSteps(savedStatus.getStatus(), (accessCheck==null)))
            .status(savedStatus.getStatus())
            .updatedAt(savedStatus.getUpdatedAt())
            .build();

        ApiResponse<OrderItemWithStatusViewModel.StatusViewModel> response = new ApiResponse<>(true, responseObject, null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    /**
     * Determines the next valid status transitions for an order item, based on current status and user role.
     * @param currentStatus The current status of the order item
     * @param isAdmin Whether the user is an admin
     * @return List of valid next statuses
     */
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
