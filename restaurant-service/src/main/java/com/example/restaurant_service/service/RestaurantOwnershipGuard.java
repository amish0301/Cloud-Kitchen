package com.example.restaurant_service.service;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.restaurant_service.Errors.custom.ResourceNotFoundException;
import com.example.restaurant_service.entity.Restaurant;
import com.example.restaurant_service.repository.RestaurantRepo;

@Component
public class RestaurantOwnershipGuard {

    private final RestaurantRepo restaurantRepo;

    public RestaurantOwnershipGuard(RestaurantRepo restaurantRepo) {
        this.restaurantRepo = restaurantRepo;
    }

    public Restaurant assertOwns(UUID restaurantId, UUID userId) {
        return restaurantRepo.findByIdAndOwnerId(restaurantId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
    }
}
