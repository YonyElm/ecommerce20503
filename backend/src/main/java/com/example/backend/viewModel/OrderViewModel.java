package com.example.backend.viewModel;

import com.example.backend.model.Order;
import com.example.backend.model.OrderItem;
import com.example.backend.model.Address;
import com.example.backend.model.Payment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderViewModel {
    private Order order;
    private List<OrderItemWithStatus> orderItemList;
    private Address shippingAddress;
    private Payment paymentMethod;
}