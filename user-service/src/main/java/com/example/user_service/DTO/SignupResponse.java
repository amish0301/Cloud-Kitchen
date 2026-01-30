package com.example.user_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponse {
    
    private String accessToken;
    private String refreshToken;
    private String email;
    private String role;
    
    @Builder.Default
    private String tokenType = "Bearer";
    
    private Long expiresIn;
    
    public SignupResponse(String accessToken, String refreshToken, String email, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.email = email;
        this.role = role;
        this.tokenType = "Bearer";
    }
}