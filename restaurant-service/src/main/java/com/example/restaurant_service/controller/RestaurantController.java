package com.example.restaurant_service.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restaurant_service.DTO.PagedResponse;
import com.example.restaurant_service.DTO.RestaurantDTO;
import com.example.restaurant_service.DTO.RestaurantDetailResponse;
import com.example.restaurant_service.DTO.RestaurantResponse;
import com.example.restaurant_service.DTO.RestaurantUpdateDTO;
import com.example.restaurant_service.entity.Restaurant;
import com.example.restaurant_service.service.RestaurantService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDetailResponse> getRestroById(@PathVariable UUID id) {
        return ResponseEntity.ok(restaurantService.getRestroById(id));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<RestaurantResponse>> getRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(restaurantService.getRestaurants(page, size));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'ADMIN')")
    public ResponseEntity<Restaurant> createRestaurant(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody RestaurantDTO dto) {
        Restaurant created = restaurantService.createRestro(dto, UUID.fromString(userId));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'ADMIN')")
    public ResponseEntity<Restaurant> updateRestaurant(
            @AuthenticationPrincipal String userId,
            Authentication auth,
            @PathVariable(value = "id") UUID restroId,
            @Valid @RequestBody RestaurantUpdateDTO dto) {

        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(restaurantService.updateRestro(restroId, dto, UUID.fromString(userId), isAdmin));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'ADMIN')")
    public ResponseEntity<String> deleteRestaurant(
            @AuthenticationPrincipal String userId,
            @PathVariable(value = "id") UUID restroId) {
        restaurantService.deleteRestroById(restroId, UUID.fromString(userId));
        return ResponseEntity.ok("Restaurant deleted");
    }
}
