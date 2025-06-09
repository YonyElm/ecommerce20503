package com.example.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @Column(name = "product_id")
    private Integer productId;

    @Min(0)
    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "last_updated")
    private Timestamp lastUpdated;
}
