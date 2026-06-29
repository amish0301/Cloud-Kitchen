package com.example.user_service.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.DTO.UserSummaryDTO;
import com.example.user_service.service.UserService;

/**
 * Service-to-service API — NOT exposed through the public gateway. Other internal
 * services (e.g. restaurant-service) call this to resolve user details.
 *
 * Authentication is the shared-secret header (X-Internal-Secret), enforced by
 * {@code InternalSecretFilter} for every /internal/** route, so no controller-level
 * check is needed here.
 */
@RestController
@RequestMapping("/internal/users")
public class InternalUserController {

    private final UserService userService;

    public InternalUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserSummaryDTO> getUserSummary(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserSummary(id));
    }
}
