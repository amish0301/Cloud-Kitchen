package com.example.restaurant_service.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restaurant_service.DTO.MenuCategory.MenuCategoryRequest;
import com.example.restaurant_service.DTO.MenuCategory.MenuCategoryResponse;
import com.example.restaurant_service.DTO.MenuCategory.MenuCategoryUpdateDTO;
import com.example.restaurant_service.service.MenuCategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/restaurant/{rid}/categories")
public class MenuCategoryController {

    @Autowired
    private final MenuCategoryService menuCategoryService;

    public MenuCategoryController(MenuCategoryService menuCategoryService) {
        this.menuCategoryService = menuCategoryService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'ADMIN')")
    public ResponseEntity<MenuCategoryResponse> createCategory(
            @AuthenticationPrincipal String userId,
            @PathVariable("rid") UUID restaurantId,
            @Valid @RequestBody MenuCategoryRequest req) {

        MenuCategoryResponse created = menuCategoryService.create(restaurantId, UUID.fromString(userId), req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{cid}")
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'ADMIN')")
    public ResponseEntity<MenuCategoryResponse> updateCategory(@AuthenticationPrincipal String userId,
            @PathVariable("rid") UUID restaurantId, @PathVariable("cid") UUID categoryId,
            @Valid @RequestBody MenuCategoryUpdateDTO dto) {

        MenuCategoryResponse updated = menuCategoryService.update(restaurantId, categoryId, UUID.fromString(userId),
                dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{cid}")
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'ADMIN')")
    public ResponseEntity<Void> deleteCategory(
            @AuthenticationPrincipal String userId,
            @PathVariable("rid") UUID restaurantId,
            @PathVariable("cid") UUID categoryId) {

        menuCategoryService.delete(restaurantId, categoryId, UUID.fromString(userId));
        return ResponseEntity.noContent().build();
    }
}
