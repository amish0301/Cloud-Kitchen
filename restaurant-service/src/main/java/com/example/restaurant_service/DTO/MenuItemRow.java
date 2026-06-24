package com.example.restaurant_service.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface MenuItemRow {

    UUID getCategoryId();
    String getCategoryName();
    UUID getItemId();
    UUID getRestaurantId();
    String getName();
    String getDescription();
    BigDecimal getPrice();
    boolean getIsVeg();
    boolean getIsAvailable();
    String getImageUrl();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
}
