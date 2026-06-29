package com.example.restaurant_service.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.restaurant_service.client.dto.UserSummary;


@FeignClient(name = "user-service", path = "/internal/users")
public interface UserServiceClient {

    @GetMapping("/{id}")
    UserSummary findById(@PathVariable("id") UUID id,
                         @RequestHeader("X-Internal-Secret") String secret);
}
