package com.example.user_service.service;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.user_service.DTO.AuthResponse;
import com.example.user_service.DTO.LoginRequest;
import com.example.user_service.DTO.SignupRequest;
import com.example.user_service.Errors.custom.InvalidArgumentException;
import com.example.user_service.Utils.JwtUtil;
import com.example.user_service.entity.User;
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
    public AuthResponse register(SignupRequest req) {
        try {
            if (userRepo.existsByEmail(req.getEmail())) {
                throw new RuntimeException("Email already exist");
            }
            User user = new User();
            user.setName(req.getName());
            user.setEmail(req.getEmail());
            user.setPassword(passwordEncoder.encode(req.getPassword()));
            user.setPhone(req.getPhone());
            user.setAddress(req.getAddress());

            // save in db
            userRepo.save(user);

            return AuthResponse.builder().email(req.getEmail()).name(user.getName()).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        try {
            User user = userRepo.findByEmail(req.getEmail()).orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

            // verify password
            if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
                throw new BadCredentialsException("Invalid email or password");
            }

            if (!user.isActive()) {
                throw new InvalidArgumentException("Account is deactivated", Map.of("email", "Account is deactivated"));
            }

            String accessToken = jwtUtil.generateToken(req.getEmail(), user.getRole().name());
            String refreshToken = jwtUtil.generateRefreshToken(req.getEmail());

            // return
            return AuthResponse.builder().email(req.getEmail()).name(user.getName()).role(user.getRole()).accessToken(accessToken).refreshToken(refreshToken).build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void logout(@RequestHeader("X-User-Id") UUID uId) {
        User user = userRepo.findById(uId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setRefreshToken(null);
        userRepo.save(user);
    }
}
