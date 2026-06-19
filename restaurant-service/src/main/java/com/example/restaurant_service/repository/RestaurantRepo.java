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

import com.example.restaurant_service.entity.Restaurant;

@Repository
public interface RestaurantRepo extends JpaRepository<Restaurant, UUID> {

    Optional<Restaurant> findById(UUID id);

    // All restaurants owned by a given user (restaurant owner).
    List<Restaurant> findByOwnerId(UUID ownerId);

    // Single restaurant scoped to its owner (authorization checks).
    Optional<Restaurant> findByIdAndOwnerId(UUID id, UUID ownerId);

    // Public listing: only active restaurants, paginated.
    Page<Restaurant> findByIsActiveTrue(Pageable pageable);

    // Listing by city for active restaurants.
    Page<Restaurant> findByCityIgnoreCaseAndIsActiveTrue(String city, Pageable pageable);

    @Query(value = "SELECT * FROM restaurants WHERE IS_ACTIVE = true AND name LIKE CONCAT('%', :term, '%')", nativeQuery = true)
    Page<Restaurant> searchRestaurants(@Param("term") String term, Pageable pageable);
}
