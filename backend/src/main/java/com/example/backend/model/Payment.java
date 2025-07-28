package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore // Fix serialization together with @JsonProperty
    private User user;

    @Column(nullable = false)
    private String name;

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

    // userId to serialize as int not as User
    @JsonProperty("userId")
    public Integer getUserId() {
        return user != null ? user.getId() : null;
    }
}