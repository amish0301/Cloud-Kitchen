package com.example.user_service.controller;
import com.example.user_service.DTO.PagedResponse;
import com.example.user_service.DTO.UserInfoDTO;
import com.example.user_service.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("info")
    public ResponseEntity<UserInfoDTO> getUserInfo(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok().body(userService.getUserInfo(userId));
    }

    @PatchMapping
    public ResponseEntity<String> updateUser(@AuthenticationPrincipal String userId, UserInfoDTO userInfo) {
        return ResponseEntity.ok().body(userService.updateUser(userId, userInfo));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok().body(userService.deleteUser(userId));
    }

    // Pageable all users info. ?page=0&size=20
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<PagedResponse<UserInfoDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok().body(userService.getAllUsers(page, size));
    }

}
