package com.example.restaurant_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restaurant_service.DTO.MenuCategoryItemsResponse;
import com.example.restaurant_service.service.MenuItemService;


@RestController
@RequestMapping("/restaurant/{rid}/items")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping
    public ResponseEntity<List<MenuCategoryItemsResponse>> getAllItems(@PathVariable("rid") UUID restaurantId) {
        return ResponseEntity.ok(menuItemService.getAllItemsWithCategory(restaurantId));
    }

    // Owner-only: create a menu item. Ownership + category-belongs-to-restaurant
    // are enforced in the service layer.
    // @PostMapping
    // @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'ADMIN')")
    // public ResponseEntity<MenuItemResponse> createItem(
    //         @AuthenticationPrincipal String userId,
    //         @PathVariable("rid") UUID restaurantId,
    //         @Valid @RequestBody MenuItemRequest req) {

    //     MenuItemResponse created =
    //             menuItemService.create(restaurantId, UUID.fromString(userId), req);
    //     return ResponseEntity.status(HttpStatus.CREATED).body(created);
    // }
}
