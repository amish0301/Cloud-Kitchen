package com.example.restaurant_service.DTO;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Set;

import com.example.restaurant_service.Utils.Constant.Cuisine;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantUpdateDTO {

    private String name;

    private String description;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
    private String phone;

    @Email(message = "Invalid email format")
    private String email;

    private String address;
    private String city;
    private String state;
    private String postalCode;

    private Double lat;
    private Double lng;

    private String logoUrl;
    private String coverImageUrl;

    private Set<Cuisine> cuisines;

    private LocalTime openTime;
    private LocalTime closeTime;

    private BigDecimal minOrderAmount;
    private Integer avgPreparationTimeMinutes;
}
