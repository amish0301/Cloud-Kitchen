package com.example.restaurant_service.DTO.MenuCategory;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.restaurant_service.entity.MenuCategory;

import lombok.Builder;
import lombok.Data;

/**
 * API view of a menu category. Never expose the JPA entity directly — its
 * {@code restaurant} association is LAZY and would break serialization outside
 * a transaction.
 */
@Data
@Builder
public class MenuCategoryResponse {

    private UUID id;
    private UUID restaurantId;
    private String name;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MenuCategoryResponse from(MenuCategory c) {
        return MenuCategoryResponse.builder()
                .id(c.getId())
                .restaurantId(c.getRestaurant().getId())
                .name(c.getName())
                .displayOrder(c.getDisplayOrder())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .build();
    }
}
