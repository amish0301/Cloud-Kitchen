package com.example.restaurant_service.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.restaurant_service.DTO.MenuItem.InternalItemResponse;
import com.example.restaurant_service.DTO.MenuItem.MenuItemRow;
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

    // Internal checkout view: item (scoped to its restaurant) + the restaurant's open
    // status, resolved in ONE joined query so order-service needs no follow-up call.
    // MenuItem holds only a raw restaurantId, so this is a theta-join to Restaurant.
    @Query("""
            SELECT new com.example.restaurant_service.DTO.MenuItem.InternalItemResponse(
                       mi.id, mi.restaurantId, mi.name, mi.price, mi.isAvailable, r.isOpen)
            FROM MenuItem mi, Restaurant r
            WHERE mi.id = :itemId
              AND mi.restaurantId = :restaurantId
              AND r.id = mi.restaurantId
            """)
    Optional<InternalItemResponse> findCheckoutView(@Param("restaurantId") UUID restaurantId,
                                                    @Param("itemId") UUID itemId);

    long countByRestaurantId(UUID restaurantId);

    // Number of items in a section — used to block deletion of non-empty categories.
    long countByCategoryId(UUID categoryId);

    @Query(value = """
            SELECT mi.category_id   AS categoryId,
                   mc.name          AS categoryName,
                   mi.id            AS itemId,
                   mi.restaurant_id AS restaurantId,
                   mi.name          AS name,
                   mi.description   AS description,
                   mi.price         AS price,
                   mi.is_veg        AS isVeg,
                   mi.is_available  AS isAvailable,
                   mi.image_url     AS imageUrl,
                   mi.created_at    AS createdAt,
                   mi.updated_at    AS updatedAt
            FROM menu_items mi
            JOIN menu_categories mc ON mc.id = mi.category_id
            WHERE mi.restaurant_id = :restaurantId
            ORDER BY mc.display_order, mc.name, mi.name
            """, nativeQuery = true)
    List<MenuItemRow> findMenuRowsByRestaurant(@Param("restaurantId") UUID restaurantId);
}
