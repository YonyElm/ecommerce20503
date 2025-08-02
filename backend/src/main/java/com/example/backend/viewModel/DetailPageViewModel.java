package com.example.backend.viewModel;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DetailPageViewModel {
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private String categoryName;
    private Integer maxQuantity;
    private String imageURL;
}
