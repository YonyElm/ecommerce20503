package com.example.backend.viewModel;

import com.example.backend.model.OrderItem;
import com.example.backend.model.OrderItemStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderItemWithStatusViewModel {
    private OrderItem orderItem;
    private List<StatusViewModel> statusList;

    @Getter
    @Setter
    @Builder
    public static class StatusViewModel {
        private OrderItemStatus.Status status;
        private List<OrderItemStatus.Status> nextSteps;
        private LocalDateTime updatedAt;
    }
}