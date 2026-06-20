package com.example.restaurant_service.DTO;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

import com.example.restaurant_service.Utils.Constant.Cuisine;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantResponse {
    private UUID id;
    private String name;
    private String description;
    private String city;
    private String state;
    private Set<Cuisine> cuisines;
    private Double averageRating;
    private Integer ratingCount;
    private String logoUrl;
    private String coverImageUrl;
    private LocalTime openTime;
    private LocalTime closeTime;
    private BigDecimal minOrderAmount;
    private Integer avgPreparationTimeMinutes;
    private boolean isOpen;
}
