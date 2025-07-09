package com.example.backend.viewModel;

import com.example.backend.model.OrderItem;
import com.example.backend.model.OrderItemStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderItemWithStatus {
    private OrderItem orderItem;
    private List<OrderItemStatus> statusList;
}