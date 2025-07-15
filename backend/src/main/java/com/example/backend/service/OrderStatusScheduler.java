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

    @Autowired
    public OrderStatusScheduler(OrderItemStatusDAO orderItemStatusDAO) {
        this.orderItemStatusDAO = orderItemStatusDAO;
    }

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

    private OrderItemStatus.Status getNextStatus(OrderItemStatus.Status currentStatus) {
        return switch (currentStatus) {
            case OrderItemStatus.Status.processing -> OrderItemStatus.Status.shipped;
            case OrderItemStatus.Status.shipped -> OrderItemStatus.Status.delivered;
            default -> currentStatus;
        };
    }
}
