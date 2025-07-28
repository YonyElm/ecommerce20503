package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    @JsonIgnore 
    private User seller;

    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonIgnore
    private Timestamp createdAt;
    
    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE", nullable = false)
    @JsonIgnore
    private Boolean isActive;

    // --- Lifecycle Callbacks to ensure default values ---
    @PrePersist // Called before a new entity is saved
    protected void onCreate() {
        if (this.isActive == null) {
            this.isActive = true;
        }
    }
    // Expose category_id for JSON serialization
    @JsonProperty("category_id")
    public Integer getCategoryId() {
        return category != null ? category.getId() : null;
    }

    @JsonProperty("seller_id")
    public Integer getSellerId() {
        return seller != null ? seller.getId() : null;
    }
}