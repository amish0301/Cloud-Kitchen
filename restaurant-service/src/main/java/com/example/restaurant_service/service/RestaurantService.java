package com.example.restaurant_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.restaurant_service.DTO.PagedResponse;
import com.example.restaurant_service.DTO.RestaurantDTO;
import com.example.restaurant_service.DTO.RestaurantDetailResponse;
import com.example.restaurant_service.DTO.RestaurantResponse;
import com.example.restaurant_service.DTO.RestaurantUpdateDTO;
import com.example.restaurant_service.Errors.custom.ResourceNotFoundException;
import com.example.restaurant_service.Utils.Helper;
import com.example.restaurant_service.client.UserServiceClient;
import com.example.restaurant_service.client.dto.UserSummary;
import com.example.restaurant_service.entity.Restaurant;
import com.example.restaurant_service.repository.RestaurantRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RestaurantService {

    private final RestaurantRepo restroRepo;
    private final UserServiceClient userServiceClient;

    @Value("${internal.api.secret}")
    private String internalApiSecret;

    public RestaurantService(RestaurantRepo restroRepo, UserServiceClient userServiceClient) {
        this.restroRepo = restroRepo;
        this.userServiceClient = userServiceClient;
    }

    /**
     * Fetch a restaurant and enrich it with the owner's public contact info,
     * resolved from user-service over HTTP (via Eureka). The lookup degrades
     * gracefully: if user-service is unreachable, the restaurant is still
     * returned with a null owner rather than failing the whole request.
     */
    @Transactional(readOnly = true)
    public RestaurantDetailResponse getRestroById(UUID id) {
        Restaurant restaurant = restroRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant Not found"));

        return RestaurantDetailResponse.from(restaurant, fetchOwner(restaurant.getOwnerId()));
    }

    private RestaurantDetailResponse.OwnerInfo fetchOwner(UUID ownerId) {
        if (ownerId == null) {
            return null;
        }
        try {
            UserSummary owner = userServiceClient.findById(ownerId, internalApiSecret);
            return RestaurantDetailResponse.OwnerInfo.builder()
                    .id(owner.getId())
                    .name(owner.getName())
                    .phone(owner.getPhone())
                    .build();
        } catch (Exception ex) {
            log.warn("Could not resolve owner {} from user-service: {}", ownerId, ex.getMessage());
            return null;
        }
    }

    public PagedResponse<RestaurantResponse> getRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("averageRating").descending());
        Page<Restaurant> restaurantPage = restroRepo.findByIsActiveTrue(pageable);

        List<RestaurantResponse> content = restaurantPage.getContent().stream()
                .map(this::toResponse)
                .toList();

        return PagedResponse.<RestaurantResponse>builder()
                .content(content)
                .page(restaurantPage.getNumber())
                .size(restaurantPage.getSize())
                .totalElements(restaurantPage.getTotalElements())
                .totalPages(restaurantPage.getTotalPages())
                .last(restaurantPage.isLast())
                .build();
    }

    private RestaurantResponse toResponse(Restaurant r) {
        return RestaurantResponse.builder()
                .id(r.getId())
                .name(r.getName())
                .description(r.getDescription())
                .city(r.getCity())
                .state(r.getState())
                .cuisines(r.getCuisines())
                .averageRating(r.getAverageRating())
                .ratingCount(r.getRatingCount())
                .logoUrl(r.getLogoUrl())
                .coverImageUrl(r.getCoverImageUrl())
                .openTime(r.getOpenTime())
                .closeTime(r.getCloseTime())
                .minOrderAmount(r.getMinOrderAmount())
                .avgPreparationTimeMinutes(r.getAvgPreparationTimeMinutes())
                .isOpen(r.isOpen())
                .build();
    }

    public Restaurant createRestro(RestaurantDTO dto, UUID ownerId) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(dto.getName());
        restaurant.setDescription(dto.getDescription());
        restaurant.setOwnerId(ownerId);
        restaurant.setPhone(dto.getPhone());
        restaurant.setEmail(dto.getEmail());
        restaurant.setAddress(dto.getAddress());
        restaurant.setCity(dto.getCity());
        restaurant.setState(dto.getState());
        restaurant.setPostalCode(dto.getPostalCode());
        restaurant.setLat(dto.getLat());
        restaurant.setLng(dto.getLng());
        restaurant.setLogoUrl(dto.getLogoUrl());
        restaurant.setCoverImageUrl(dto.getCoverImageUrl());
        if (dto.getCuisines() != null) {
            restaurant.setCuisines(dto.getCuisines());
        }
        restaurant.setOpenTime(dto.getOpenTime());
        restaurant.setCloseTime(dto.getCloseTime());
        restaurant.setMinOrderAmount(dto.getMinOrderAmount());
        restaurant.setAvgPreparationTimeMinutes(dto.getAvgPreparationTimeMinutes());
        return restroRepo.save(restaurant);
    }

    @Transactional
    public Restaurant updateRestro(UUID restroId, RestaurantUpdateDTO dto, UUID ownerId, boolean isAdmin) {
        Restaurant restaurant = isAdmin ? restroRepo.findById(restroId).orElseThrow()
                : restroRepo.findByIdAndOwnerId(restroId, ownerId)
                        .orElseThrow(() -> new ResourceNotFoundException("Restaurant Not found"));

        Helper.applyIfPresent(dto.getName(), restaurant::setName);
        Helper.applyIfPresent(dto.getDescription(), restaurant::setDescription);
        Helper.applyIfPresent(dto.getPhone(), restaurant::setPhone);
        Helper.applyIfPresent(dto.getEmail(), restaurant::setEmail);
        Helper.applyIfPresent(dto.getAddress(), restaurant::setAddress);
        Helper.applyIfPresent(dto.getCity(), restaurant::setCity);
        Helper.applyIfPresent(dto.getState(), restaurant::setState);
        Helper.applyIfPresent(dto.getPostalCode(), restaurant::setPostalCode);
        Helper.applyIfPresent(dto.getLat(), restaurant::setLat);
        Helper.applyIfPresent(dto.getLng(), restaurant::setLng);
        Helper.applyIfPresent(dto.getLogoUrl(), restaurant::setLogoUrl);
        Helper.applyIfPresent(dto.getCoverImageUrl(), restaurant::setCoverImageUrl);
        Helper.applyIfPresent(dto.getCuisines(), restaurant::setCuisines);
        Helper.applyIfPresent(dto.getOpenTime(), restaurant::setOpenTime);
        Helper.applyIfPresent(dto.getCloseTime(), restaurant::setCloseTime);
        Helper.applyIfPresent(dto.getMinOrderAmount(), restaurant::setMinOrderAmount);
        Helper.applyIfPresent(dto.getAvgPreparationTimeMinutes(), restaurant::setAvgPreparationTimeMinutes);

        return restroRepo.save(restaurant);
    }

    public void deleteRestroById(UUID restroId, UUID ownerId) {
        Restaurant restaurant = restroRepo.findByIdAndOwnerId(restroId, ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant Not found"));
        restroRepo.delete(restaurant);
    }

}
