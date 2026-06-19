package com.example.restaurant_service.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.example.restaurant_service.Utils.Constant.Cuisine;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "restaurants")
@Data
@NoArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private UUID ownerId;

    private String phone;
    private String email;

    private String address;
    private String city;
    private String state;
    private String postalCode;

    private Double lat;
    private Double lng;

    // ── Branding ── //
    private String logoUrl;
    private String coverImageUrl;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "restaurant_cuisines",
        joinColumns = @JoinColumn(name = "restaurant_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "cuisine")
    private Set<Cuisine> cuisines = new HashSet<>();

    @Column(nullable = false)
    private Double averageRating = 0.0;

    @Column(nullable = false)
    private Integer ratingCount = 0;

    private LocalTime openTime;
    private LocalTime closeTime;

    @Column(precision = 10, scale = 2)
    private BigDecimal minOrderAmount;

    private Integer avgPreparationTimeMinutes;

    // ── Status ──
    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private boolean isOpen = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
