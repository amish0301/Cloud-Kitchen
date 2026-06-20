package com.example.restaurant_service.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurant_service.entity.MenuItem;

@Repository
public interface MenuItemRepo extends JpaRepository<MenuItem, UUID> {

    // Full menu of a restaurant, paginated. Uses the denormalized restaurantId column.
    Page<MenuItem> findByRestaurantId(UUID restaurantId, Pageable pageable);

    // Only items currently available (customer-facing menu).
    Page<MenuItem> findByRestaurantIdAndIsAvailableTrue(UUID restaurantId, Pageable pageable);

    // Items within a section. Resolves to MenuItem.category.id.
    List<MenuItem> findByCategoryId(UUID categoryId);

    // Fetch one item but scoped to its restaurant (authorization / integrity).
    Optional<MenuItem> findByIdAndRestaurantId(UUID id, UUID restaurantId);

    long countByRestaurantId(UUID restaurantId);
}
