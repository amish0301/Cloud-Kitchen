package com.example.restaurant_service.service;

import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.restaurant_service.DTO.MenuCategoryRequest;
import com.example.restaurant_service.DTO.MenuCategoryResponse;
import com.example.restaurant_service.DTO.MenuCategoryUpdateDTO;
import com.example.restaurant_service.Errors.custom.ConflictException;
import com.example.restaurant_service.Errors.custom.ResourceNotFoundException;
import com.example.restaurant_service.Utils.Helper;
import com.example.restaurant_service.entity.MenuCategory;
import com.example.restaurant_service.entity.Restaurant;
import com.example.restaurant_service.repository.MenuCategoryRepo;
import com.example.restaurant_service.repository.MenuItemRepo;

@Service
public class MenuCategoryService {

    private final MenuCategoryRepo categoryRepo;
    private final MenuItemRepo menuItemRepo;
    private final RestaurantOwnershipGuard ownershipGuard;

    public MenuCategoryService(MenuCategoryRepo categoryRepo, MenuItemRepo menuItemRepo, RestaurantOwnershipGuard ownershipGuard) {
        this.categoryRepo = categoryRepo;
        this.menuItemRepo = menuItemRepo;
        this.ownershipGuard = ownershipGuard;
    }

    @Transactional
    public MenuCategoryResponse create(UUID restaurantId, UUID userId, MenuCategoryRequest req) {
        Restaurant restaurant = ownershipGuard.assertOwns(restaurantId, userId);

        MenuCategory category = new MenuCategory();
        category.setRestaurant(restaurant);
        category.setName(req.getName());
        category.setDisplayOrder(req.getDisplayOrder());

        return MenuCategoryResponse.from(categoryRepo.save(category));
    }

    @Transactional
    public MenuCategoryResponse update(UUID restaurantId, UUID categoryId, UUID userId, MenuCategoryUpdateDTO dto) {
        ownershipGuard.assertOwns(restaurantId, userId);
        MenuCategory category = loadScoped(categoryId, restaurantId);

        Helper.applyIfPresent(dto.getName(), category::setName);
        Helper.applyIfPresent(dto.getDisplayOrder(), category::setDisplayOrder);

        return MenuCategoryResponse.from(categoryRepo.save(category));
    }

    @Transactional
    public void delete(UUID restaurantId, UUID categoryId, UUID userId) {
        ownershipGuard.assertOwns(restaurantId, userId);
        MenuCategory category = loadScoped(categoryId, restaurantId);

        long itemCount = menuItemRepo.countByCategoryId(categoryId);
        if (itemCount > 0) {
            throw new ConflictException(
                    "Cannot delete category: it still has " + itemCount
                    + " menu item(s). Remove or move them first.");
        }
        categoryRepo.delete(category);
    }

    // Loads a category scoped to its restaurant — guarantees the category actually
    // belongs to the path's restaurant, not just any restaurant.
    private MenuCategory loadScoped(UUID categoryId, UUID restaurantId) {
        return categoryRepo.findByIdAndRestaurantId(categoryId, restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }
}
