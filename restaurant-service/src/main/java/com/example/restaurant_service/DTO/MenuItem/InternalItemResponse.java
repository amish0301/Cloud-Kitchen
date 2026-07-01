package com.example.restaurant_service.DTO.MenuItem;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Minimal item view for the internal service-to-service checkout call. order-service
 * resolves an item's price + availability here (and whether its restaurant is open)
 * before placing an order, so the fields are deliberately the bare checkout contract.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InternalItemResponse {

    private UUID itemId;
    private UUID restaurantId;
    private String name;
    private BigDecimal price;

    @JsonProperty("isAvailable")
    private boolean isAvailable;

    @JsonProperty("restaurantIsOpen")
    private boolean restaurantIsOpen;
}
