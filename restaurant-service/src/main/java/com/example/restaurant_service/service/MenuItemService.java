package com.example.restaurant_service.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.restaurant_service.DTO.MenuCategoryItemsResponse;
import com.example.restaurant_service.DTO.MenuItemResponse;
import com.example.restaurant_service.DTO.MenuItemRow;
import com.example.restaurant_service.repository.MenuItemRepo;

@Service
public class MenuItemService {

    private final MenuItemRepo itemRepo;

    public MenuItemService(MenuItemRepo itemRepo) {
        this.itemRepo = itemRepo;
    }

    /**
     * Full menu grouped by category. The native query returns flat rows (one per
     * item, each carrying its category); we group them by categoryId in memory.
     * A LinkedHashMap preserves the query's ORDER BY (display order), so sections
     * come out in the order the owner configured.
     */
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
