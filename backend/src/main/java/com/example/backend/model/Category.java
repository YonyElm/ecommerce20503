package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> products;

    // Expose list of product IDs for serialization
    @JsonProperty("productIds")
    public List<Integer> getProductIds() {
        if (products == null) {
            return new ArrayList<>();
        } else {
            return products.stream()
                    .map(Product::getId)
                    .collect(Collectors.toList());
        }
    }

    // --- Lifecycle Callback to ensure default values ---
    @PrePersist
    @PreUpdate
    protected void ensureDefaults() {
        if (this.products == null) {
            this.products = new ArrayList<>();
        }
    }
}
