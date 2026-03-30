package com.example.user_service.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class AuthResponse {
    private UUID id;
    private String name;
    private String email;
    private String role;
    private String accessToken;
    private String refreshToken;
}