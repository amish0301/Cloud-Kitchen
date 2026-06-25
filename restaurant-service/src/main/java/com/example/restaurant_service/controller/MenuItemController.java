package com.example.restaurant_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restaurant_service.DTO.MenuCategory.MenuCategoryItemsResponse;
import com.example.restaurant_service.DTO.MenuItem.MenuItemRequest;
import com.example.restaurant_service.DTO.MenuItem.MenuItemResponse;
import com.example.restaurant_service.DTO.MenuItem.MenuItemUpdateDTO;
import com.example.restaurant_service.service.MenuItemService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/restaurant/{rid}")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping("/menu")
    public ResponseEntity<List<MenuCategoryItemsResponse>> getAllItems(@PathVariable("rid") UUID restaurantId) {
        return ResponseEntity.ok(menuItemService.getAllItemsWithCategory(restaurantId));
    }

    // Owner-only: create a menu item. Ownership + category-belongs-to-restaurant
    // are enforced in the service layer.
    @PostMapping("/categories/{cid}/items")
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'ADMIN')")
    public ResponseEntity<MenuItemResponse> createItem(
            @AuthenticationPrincipal String userId,
            @PathVariable("rid") UUID restaurantId,
            @PathVariable("cid") UUID categoryId,
            @Valid @RequestBody MenuItemRequest req) {

        MenuItemResponse created =
                menuItemService.create(restaurantId, categoryId, UUID.fromString(userId), req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // DELETE Menu Item
    @DeleteMapping("/categories/{cid}/items/{iid}")
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'ADMIN')")
    public ResponseEntity<Void> deleteItem(
            @AuthenticationPrincipal String userId,
            @PathVariable("rid") UUID restaurantId,
            @PathVariable("cid") UUID categoryId,
            @PathVariable("iid") UUID itemId) {

        menuItemService.deleteMenuItem(restaurantId, categoryId, itemId, UUID.fromString(userId));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/items/{iid}")
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'ADMIN')")
    public ResponseEntity<MenuItemResponse> updateItem(
            @AuthenticationPrincipal String userId,
            @PathVariable("rid") UUID restaurantId,
            @PathVariable("iid") UUID itemId,
            @Valid @RequestBody MenuItemUpdateDTO req) {

        MenuItemResponse updated =
                menuItemService.update(restaurantId, itemId, UUID.fromString(userId), req);
        return ResponseEntity.ok(updated);
    }

    // Kafka event consume and update based on availability
    @PatchMapping("/items/{iid}/availability")
    public ResponseEntity<Void> updateItemAvailability(
            @PathVariable("rid") UUID restaurantId,
            @PathVariable("iid") UUID itemId,
            boolean isAvailable) {

        menuItemService.updateMenuItemAvailability(restaurantId, itemId, null, isAvailable);
        return ResponseEntity.noContent().build();
        
    }
}
