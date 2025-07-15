package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_item_status")
public class OrderItemStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Link to the order item
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Status {
        processing,
        shipped,
        delivered,
        cancelled,
        returned
    }

    // --- Lifecycle Callbacks to ensure default values ---
    @PrePersist // Called before a new entity is saved
    protected void onCreate() {
        if (this.status == null) {
            this.status = Status.processing;
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
    }
}