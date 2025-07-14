package com.example.backend.viewModel;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartPageItemViewModel {
    private Integer itemId;
    private Integer productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    // private String image id
}
