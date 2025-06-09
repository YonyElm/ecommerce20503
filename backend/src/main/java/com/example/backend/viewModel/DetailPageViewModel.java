package com.example.backend.viewModel;

import com.example.backend.model.Category;
import com.example.backend.model.Inventory;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DetailPageViewModel {
    private String name;
    private String description;
    private BigDecimal price;
    private String categoryName;
    private Integer maxQuantity;
}
