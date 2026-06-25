package com.example.restaurant_service.DTO.MenuItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.restaurant_service.entity.MenuItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API view of a menu item. Built from the entity rather than serializing it
 * directly, since {@code category} is a LAZY association.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemResponse {

    private UUID id;
    private UUID restaurantId;
    private UUID categoryId;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean isVeg;
    private boolean isAvailable;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MenuItemResponse from(MenuItem m) {
        return MenuItemResponse.builder()
                .id(m.getId())
                .restaurantId(m.getRestaurantId())
                .categoryId(m.getCategory().getId())
                .name(m.getName())
                .description(m.getDescription())
                .price(m.getPrice())
                .isVeg(m.isVeg())
                .isAvailable(m.isAvailable())
                .imageUrl(m.getImageUrl())
                .createdAt(m.getCreatedAt())
                .updatedAt(m.getUpdatedAt())
                .build();
    }
}
