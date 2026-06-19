package com.example.restaurant_service.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restaurant_service.entity.Restaurant;
import com.example.restaurant_service.repository.RestaurantRepo;

import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
    
    private final RestaurantRepo restroRepo;

    @Autowired
    public RestaurantController(RestaurantRepo restroRepo) {
        this.restroRepo = restroRepo;
    }

    // @GetMapping("/restaurants/{id}")
    // public ResponseEntity<Restaurant> getRestroById(@PathVariable @NotNull UUID id) {
    //     // return ResponseEntity.ok(restroRepo.findById(id));
    // }
}
