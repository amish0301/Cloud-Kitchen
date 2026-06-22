package com.example.restaurant_service.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurant_service.entity.MenuCategory;

@Repository
public interface MenuCategoryRepo extends JpaRepository<MenuCategory, UUID> {

    // All sections of a restaurant's menu, ordered for display. Traverses MenuCategory.restaurant.id.
    List<MenuCategory> findByRestaurantIdOrderByDisplayOrderAsc(UUID restaurantId);

    // Single category scoped to its restaurant (authorization / integrity).
    Optional<MenuCategory> findByIdAndRestaurantId(UUID id, UUID restaurantId);

    boolean existsByIdAndRestaurantId(UUID id, UUID restaurantId);
}
