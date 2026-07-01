package com.example.restaurant_service.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restaurant_service.DTO.MenuItem.InternalItemResponse;
import com.example.restaurant_service.service.MenuItemService;

/**
 * Service-to-service API — NOT exposed through the public gateway. Other internal
 * services (e.g. order-service) call this to resolve menu-item details at checkout.
 *
 * Authentication is the shared-secret header (X-Internal-Secret), enforced by
 * {@code InternalSecretFilter} on the dedicated /internal/** security chain, so no
 * controller-level check is needed here.
 */
@RestController
@RequestMapping("/internal/restaurants")
public class InternalController {

    private final MenuItemService itemService;

    public InternalController(MenuItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{rId}/items/{iid}")
    public ResponseEntity<InternalItemResponse> getItem(@PathVariable UUID rId, @PathVariable UUID iid) {
        return ResponseEntity.ok(itemService.getItemForCheckout(rId, iid));
    }
}
