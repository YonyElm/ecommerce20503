package com.example.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Builder
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Product name is required")
    @Column(nullable = false)
    private String name;

    @Size(max = 1000, message = "Description can't exceed 1000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price format is invalid")
    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

//    // Used for JPA bean injection
//    public Product() {}
//
//    public Product(int id, String name, String description, BigDecimal price, Category category, Timestamp createdAt) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.price = price;
//        this.category = category;
//        this.createdAt = createdAt;
//    }
}
