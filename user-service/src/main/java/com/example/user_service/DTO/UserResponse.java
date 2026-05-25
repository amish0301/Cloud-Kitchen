package com.example.user_service.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.user_service.Utils.Constant.Role;
import com.example.user_service.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private UUID id;
    private String name;
    private String email;
    private String phone;
    private Role role;
    private String profileImageUrl;
    private String address;
    private String city;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .profileImageUrl(user.getProfileImageUrl())
                .address(user.getAddress())
                .city(user.getCity())
                .createdAt(user.getCreatedAt())
                .lastLogin(user.getLastLogin())
                .build();
    }
}
