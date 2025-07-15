package com.example.backend.dao;

import com.example.backend.model.OrderItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemStatusDAO extends JpaRepository<OrderItemStatus, Integer> {
    // Get only the LATEST status for a given order item
    OrderItemStatus findTopByOrderItemIdOrderByUpdatedAtDesc(int orderItemId);

    // Get only the LATEST status for each order_item_id (Assuming latest ID indicates latest INSERT time)
    @Query("SELECT ois FROM OrderItemStatus ois WHERE ois.id IN (" +
            "    SELECT MAX(ois2.id) FROM OrderItemStatus ois2 GROUP BY ois2.orderItem.id" +
            ") AND ois.status IN :eligibleStatuses " + // Filter by statuses that can still change
            "AND ois.updatedAt < :cutoffTimestamp") // Filter by time (Unix timestamp comparison)
    List<OrderItemStatus> findLatestEligibleStatusesForUpdate(
            @Param("eligibleStatuses") List<OrderItemStatus.Status> eligibleStatuses,
            @Param("cutoffTimestamp") LocalDateTime cutoffTimestamp);
}