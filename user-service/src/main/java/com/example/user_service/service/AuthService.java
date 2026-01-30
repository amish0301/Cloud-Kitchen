package com.example.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.user_service.DTO.LoginRequest;
import com.example.user_service.DTO.SignupRequest;
import com.example.user_service.DTO.SignupResponse;
import com.example.user_service.Utils.JwtUtil;
import com.example.user_service.repository.UserRepo;

@Service
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;


    @Transactional
    public SignupResponse register(SignupRequest req) {
        try {
            // register user in db
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return SignupResponse.builder().email(req.getEmail()).build();
    }

    public String login(LoginRequest req) {
        return "Login Succesful";
    }
}
