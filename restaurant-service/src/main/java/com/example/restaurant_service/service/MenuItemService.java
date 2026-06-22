package com.example.restaurant_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restaurant_service.DTO.MenuItemResponse;
import com.example.restaurant_service.repository.MenuItemRepo;

@Service
public class MenuItemService {
    @Autowired
    private final MenuItemRepo itemRepo;
    public MenuItemService(MenuItemRepo itemRepo) {
        this.itemRepo = itemRepo;
    }
    
    // get all items
    // public List<MenuItemResponse> getAllItems(UUID rid) {
        
    // }
}
