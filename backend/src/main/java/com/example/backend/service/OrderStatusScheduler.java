package com.example.backend.service;

import com.example.backend.dao.OrderItemStatusDAO;
import com.example.backend.model.OrderItemStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This class automatically updates the status of order items every 5 minutes
 * to mimic real-world order progression (e.g., PROCESSING → SHIPPED → DELIVERED),
 * without requiring manual bookkeeping or actual shipment integration.
 */
@Service
public class OrderStatusScheduler {

    final static int MINUTES = 5;
    final static List<OrderItemStatus.Status> ELIGIBLE_STATUS = List.of(OrderItemStatus.Status.processing,
                                                                        OrderItemStatus.Status.shipped);
    private final OrderItemStatusDAO orderItemStatusDAO;


    /**
     * Constructor for OrderStatusScheduler.
     * @param orderItemStatusDAO DAO for order item status data access
     */
    @Autowired
    public OrderStatusScheduler(OrderItemStatusDAO orderItemStatusDAO) {
        this.orderItemStatusDAO = orderItemStatusDAO;
    }


    /**
     * Periodically updates the status of eligible order items to the next status (e.g., PROCESSING → SHIPPED → DELIVERED).
     * Runs every MINUTES interval as scheduled.
     */
    @Transactional
    @Scheduled(fixedRate = MINUTES * 60 * 1000) // every X minutes
    public void updateOrderItemStatuses() {
        List<OrderItemStatus> staleStatuses = orderItemStatusDAO
                .findLatestEligibleStatusesForUpdate(ELIGIBLE_STATUS, LocalDateTime.now().minusMinutes(MINUTES));
        for (OrderItemStatus status : staleStatuses) {
            OrderItemStatus.Status current = status.getStatus();
            OrderItemStatus.Status next = getNextStatus(current);
            if (!current.equals(next)) {
                OrderItemStatus newStatus = OrderItemStatus.builder()
                        .status(next)
                        .orderItem(status.getOrderItem())
                        .build();
                orderItemStatusDAO.save(newStatus);
            }
        }
    }

    /**
     * Determines the next status for an order item based on its current status.
     * @param currentStatus The current status of the order item
     * @return The next status, or the current status if no further progression
     */
    private OrderItemStatus.Status getNextStatus(OrderItemStatus.Status currentStatus) {
        return switch (currentStatus) {
            case OrderItemStatus.Status.processing -> OrderItemStatus.Status.shipped;
            case OrderItemStatus.Status.shipped -> OrderItemStatus.Status.delivered;
            default -> currentStatus;
        };
    }
}
