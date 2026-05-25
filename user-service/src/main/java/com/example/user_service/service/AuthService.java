package com.example.user_service.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.user_service.DTO.AuthResponse;
import com.example.user_service.DTO.LoginRequest;
import com.example.user_service.DTO.SignupRequest;
import com.example.user_service.Errors.custom.InvalidArgumentException;
import com.example.user_service.Utils.JwtUtil;
import com.example.user_service.auth.CustomUserDetails;
import com.example.user_service.entity.User;
import com.example.user_service.repository.UserRepo;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(SignupRequest req) {
        if (userRepo.existsByEmail(req.getEmail())) {
            throw new InvalidArgumentException("Email already exists", Map.of("email", "Email already exists"));
        }
        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setPhone(req.getPhone());
        user.setAddress(req.getAddress());

        userRepo.save(user);

        log.info("User registered: {}", user);

        return AuthResponse.builder().email(req.getEmail()).name(user.getName()).build();
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        User user = principal.getUser();

        String accessToken = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        log.info("IN /login tokens generated accessToken : {} & refreshToken : {}", accessToken, refreshToken);

        return AuthResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void logout(UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new InvalidArgumentException("User not found", Map.of("userId", "User not found")));
        user.setRefreshToken(null);
        userRepo.save(user);
    }
}
