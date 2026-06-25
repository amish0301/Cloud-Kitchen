package com.example.restaurant_service.DTO.MenuCategory;

import java.util.List;
import java.util.UUID;

import com.example.restaurant_service.DTO.MenuItem.MenuItemResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuCategoryItemsResponse {

    private UUID categoryId;
    private String name;
    private List<MenuItemResponse> items;
}
