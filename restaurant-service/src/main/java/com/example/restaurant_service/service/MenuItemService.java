package com.example.restaurant_service.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.restaurant_service.DTO.MenuCategory.MenuCategoryItemsResponse;
import com.example.restaurant_service.DTO.MenuItem.InternalItemResponse;
import com.example.restaurant_service.DTO.MenuItem.MenuItemRequest;
import com.example.restaurant_service.DTO.MenuItem.MenuItemResponse;
import com.example.restaurant_service.DTO.MenuItem.MenuItemRow;
import com.example.restaurant_service.DTO.MenuItem.MenuItemUpdateDTO;
import com.example.restaurant_service.Errors.custom.ResourceNotFoundException;
import com.example.restaurant_service.Utils.Helper;
import com.example.restaurant_service.entity.MenuCategory;
import com.example.restaurant_service.entity.MenuItem;
import com.example.restaurant_service.repository.MenuCategoryRepo;
import com.example.restaurant_service.repository.MenuItemRepo;

@Service
public class MenuItemService {

    private final MenuItemRepo itemRepo;
    private final MenuCategoryRepo categoryRepo;
    private final RestaurantOwnershipGuard ownershipGuard;

    public MenuItemService(MenuItemRepo itemRepo,
                           MenuCategoryRepo categoryRepo,
                           RestaurantOwnershipGuard ownershipGuard) {
        this.itemRepo = itemRepo;
        this.categoryRepo = categoryRepo;
        this.ownershipGuard = ownershipGuard;
    }

    @Transactional
    public MenuItemResponse create(UUID restaurantId, UUID categoryId, UUID userId, MenuItemRequest req) {
        ownershipGuard.assertOwns(restaurantId, userId);

        MenuCategory category = categoryRepo.findByIdAndRestaurantId(categoryId, restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        MenuItem item = new MenuItem();
        item.setCategory(category);
        item.setRestaurantId(restaurantId);
        item.setName(req.getName());
        item.setDescription(req.getDescription());
        item.setPrice(req.getPrice());
        item.setVeg(req.isVeg());
        item.setAvailable(req.getIsAvailable() == null || req.getIsAvailable());
        item.setImageUrl(req.getImageUrl());

        return MenuItemResponse.from(itemRepo.save(item));
    }

    @Transactional
    public MenuItemResponse update(UUID restaurantId, UUID itemId, UUID userId, MenuItemUpdateDTO dto) {
        ownershipGuard.assertOwns(restaurantId, userId);

        MenuItem item = itemRepo.findByIdAndRestaurantId(itemId, restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        Helper.applyIfPresent(dto.getName(), item::setName);
        Helper.applyIfPresent(dto.getDescription(), item::setDescription);
        Helper.applyIfPresent(dto.getPrice(), item::setPrice);
        Helper.applyIfPresent(dto.getIsVeg(), item::setVeg);
        Helper.applyIfPresent(dto.getImageUrl(), item::setImageUrl);

        // Re-parent only when the category actually changes.
        if (dto.getCategoryId() != null && !dto.getCategoryId().equals(item.getCategory().getId())) {
            MenuCategory newCategory = categoryRepo.findByIdAndRestaurantId(dto.getCategoryId(), restaurantId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            item.setCategory(newCategory);
        }

        return MenuItemResponse.from(itemRepo.save(item));
    }

    public boolean updateMenuItemAvailability(UUID restaurantId, UUID itemId, UUID userId, boolean isAvailable) {
        MenuItem item = itemRepo.findByIdAndRestaurantId(itemId, restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));
        item.setAvailable(isAvailable);
        itemRepo.save(item);
        return true;
    }

    @Transactional
    public String deleteMenuItem(UUID restaurantId, UUID categoryId, UUID itemId, UUID userId) {
        ownershipGuard.assertOwns(restaurantId, userId);
        MenuItem item = itemRepo.findByIdAndRestaurantId(itemId, restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));
        itemRepo.delete(item);
        return String.format("%s Menu Item Deleted", item.getName());
    }

    @Transactional(readOnly = true)
    public InternalItemResponse getItemForCheckout(UUID restaurantId, UUID itemId) {
        return itemRepo.findCheckoutView(restaurantId, itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));
    }

    @Transactional(readOnly = true)
    public List<MenuCategoryItemsResponse> getAllItemsWithCategory(UUID restroId) {
        Map<UUID, List<MenuItemRow>> rowsByCategory = itemRepo.findMenuRowsByRestaurant(restroId).stream()
                .collect(Collectors.groupingBy(
                        MenuItemRow::getCategoryId,
                        LinkedHashMap::new,
                        Collectors.toList()));

        return rowsByCategory.values().stream()
                .map(rows -> MenuCategoryItemsResponse.builder()
                        .categoryId(rows.get(0).getCategoryId())
                        .name(rows.get(0).getCategoryName())
                        .items(rows.stream().map(MenuItemService::toItemResponse).toList())
                        .build())
                .toList();
    }

    // Maps a flat projection row to the item view DTO.
    private static MenuItemResponse toItemResponse(MenuItemRow row) {
        return MenuItemResponse.builder()
                .id(row.getItemId())
                .restaurantId(row.getRestaurantId())
                .categoryId(row.getCategoryId())
                .name(row.getName())
                .description(row.getDescription())
                .price(row.getPrice())
                .isVeg(row.getIsVeg())
                .isAvailable(row.getIsAvailable())
                .imageUrl(row.getImageUrl())
                .createdAt(row.getCreatedAt())
                .updatedAt(row.getUpdatedAt())
                .build();
    }
}
