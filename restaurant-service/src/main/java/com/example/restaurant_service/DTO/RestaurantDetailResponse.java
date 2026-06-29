package com.example.restaurant_service.DTO;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

import com.example.restaurant_service.Utils.Constant.Cuisine;
import com.example.restaurant_service.entity.Restaurant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantDetailResponse {

    private UUID id;
    private String name;
    private String description;
    private String phone;
    private String email;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private Double lat;
    private Double lng;
    private String logoUrl;
    private String coverImageUrl;
    private Set<Cuisine> cuisines;
    private Double averageRating;
    private Integer ratingCount;
    private LocalTime openTime;
    private LocalTime closeTime;
    private BigDecimal minOrderAmount;
    private Integer avgPreparationTimeMinutes;
    private boolean isActive;
    private boolean isOpen;

    private OwnerInfo owner;

    @Data
    @Builder
    public static class OwnerInfo {
        private UUID id;
        private String name;
        private String phone;
    }

    /** Build from the restaurant entity; owner may be null. */
    public static RestaurantDetailResponse from(Restaurant r, OwnerInfo owner) {
        return RestaurantDetailResponse.builder()
                .id(r.getId())
                .name(r.getName())
                .description(r.getDescription())
                .phone(r.getPhone())
                .email(r.getEmail())
                .address(r.getAddress())
                .city(r.getCity())
                .state(r.getState())
                .postalCode(r.getPostalCode())
                .lat(r.getLat())
                .lng(r.getLng())
                .logoUrl(r.getLogoUrl())
                .coverImageUrl(r.getCoverImageUrl())
                .cuisines(r.getCuisines())
                .averageRating(r.getAverageRating())
                .ratingCount(r.getRatingCount())
                .openTime(r.getOpenTime())
                .closeTime(r.getCloseTime())
                .minOrderAmount(r.getMinOrderAmount())
                .avgPreparationTimeMinutes(r.getAvgPreparationTimeMinutes())
                .isActive(r.isActive())
                .isOpen(r.isOpen())
                .owner(owner)
                .build();
    }
}
