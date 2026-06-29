package com.example.user_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.user_service.DTO.AddressDTO;
import com.example.user_service.DTO.PagedResponse;
import com.example.user_service.DTO.UserInfoDTO;
import com.example.user_service.DTO.UserSummaryDTO;
import com.example.user_service.Errors.custom.ResourceNotFoundException;
import com.example.user_service.entity.User;
import com.example.user_service.repository.UserRepo;

@Service
public class UserService {
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public UserInfoDTO getUserInfo(String uId) {
        User user = userRepo.findById(UUID.fromString(uId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + uId));

        return toUserInfoDTO(user);
    }

    // Internal service-to-service lookup (used by other services via Feign).
    public UserSummaryDTO getUserSummary(UUID id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));

        return UserSummaryDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .build();
    }

    @Transactional
    public String updateUser(String userId, UserInfoDTO userInfo) {
        User user = userRepo.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        if(userInfo.getEmail() != null) {
            throw new IllegalArgumentException("Email cannot be updated");
        }

        if (userInfo.getName() != null) {
            user.setName(userInfo.getName());
        }
        if (userInfo.getPhone() != null) {
            user.setPhone(userInfo.getPhone());
        }

        AddressDTO address = userInfo.getAddress();
        if (address != null) {
            if (address.getAddress() != null) {
                user.setAddress(address.getAddress());
            }
            if (address.getCity() != null) {
                user.setCity(address.getCity());
            }
        }
        return "User info updated successfully: " + userId;
    }

    // Delete user
    public String deleteUser(String uId) {
        User usr = userRepo.findById(UUID.fromString(uId)).orElseThrow(() -> new ResourceNotFoundException("User not found: " + uId));
        usr.setActive(false);
        userRepo.save(usr);
        return String.format("User deleted Successfully");
    }
    
    public PagedResponse<UserInfoDTO> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> userPage = userRepo.getAllUsers(pageable);

        List<UserInfoDTO> content = userPage.getContent().stream()
                .map(this::toUserInfoDTO)
                .toList();

        return PagedResponse.<UserInfoDTO>builder()
                .content(content)
                .page(userPage.getNumber())
                .size(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .last(userPage.isLast())
                .build();
    }

    private UserInfoDTO toUserInfoDTO(User user) {
        return UserInfoDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(AddressDTO.builder()
                        .city(user.getCity())
                        .address(user.getAddress())
                        .build())
                .build();
    }
}
