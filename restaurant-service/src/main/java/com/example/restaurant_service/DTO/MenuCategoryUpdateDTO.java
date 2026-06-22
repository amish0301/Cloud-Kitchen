package com.example.restaurant_service.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuCategoryUpdateDTO {

    @Size(min = 2, max = 50, message = "Name must be 2-50 characters")
    private String name;

    @PositiveOrZero(message = "displayOrder must be zero or positive")
    private Integer displayOrder;
}
