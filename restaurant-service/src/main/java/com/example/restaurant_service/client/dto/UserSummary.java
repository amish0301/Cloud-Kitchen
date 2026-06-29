package com.example.restaurant_service.client.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response contract for user-service's internal API ({@code GET /internal/users/{id}}).
 * Mirrors user-service's {@code UserSummaryDTO}. Tolerant of extra fields so the
 * provider can evolve its payload without breaking this client.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSummary {
    private UUID id;
    private String name;
    private String phone;
    private String email;
}
