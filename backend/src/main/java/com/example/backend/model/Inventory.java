package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inventory")
public class Inventory {

    @Id
    @Column(name = "product_id")
    private int productId;

    @Min(0)
    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "last_updated")
    @UpdateTimestamp
    @JsonIgnore
    private Timestamp lastUpdated;
}
