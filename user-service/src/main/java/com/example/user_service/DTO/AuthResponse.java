package com.example.user_service.DTO;

import java.util.UUID;

import com.example.user_service.Utils.Constant.Role;
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
public class AuthResponse {
    private UUID id;
    private String name;
    private String email;
    private Role role;
    private String accessToken;
    private String refreshToken;
}