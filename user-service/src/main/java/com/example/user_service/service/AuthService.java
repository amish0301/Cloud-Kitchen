package com.example.user_service.service;

import org.springframework.stereotype.Service;

import com.example.user_service.DTO.LoginRequest;
import com.example.user_service.DTO.SignupRequest;

@Service
public class AuthService {

    public String register(SignupRequest req) {
        // db register 
        return "User registered successfully";
    }

    public String login(LoginRequest req) {
        return "Login Succesful";
    }
    
}
