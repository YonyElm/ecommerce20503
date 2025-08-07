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
    /**
     * Finds the latest status for a given order item by order of update time.
     * @param orderItemId The ID of the order item
     * @return The latest OrderItemStatus for the order item
     */
    OrderItemStatus findTopByOrderItemIdOrderByUpdatedAtDesc(int orderItemId);

    /**
     * Finds the latest eligible statuses for update for each order item, filtered by status and cutoff time.
     * @param eligibleStatuses List of statuses that are eligible for update
     * @param cutoffTimestamp The cutoff LocalDateTime for status update
     * @return List of OrderItemStatus objects eligible for update
     */
    @Query("SELECT ois FROM OrderItemStatus ois WHERE ois.id IN (" +
            "    SELECT MAX(ois2.id) FROM OrderItemStatus ois2 GROUP BY ois2.orderItem.id" +
            ") AND ois.status IN :eligibleStatuses " + // Filter by statuses that can still change
            "AND ois.updatedAt < :cutoffTimestamp") // Filter by time (Unix timestamp comparison)
    List<OrderItemStatus> findLatestEligibleStatusesForUpdate(
            @Param("eligibleStatuses") List<OrderItemStatus.Status> eligibleStatuses,
            @Param("cutoffTimestamp") LocalDateTime cutoffTimestamp);
}