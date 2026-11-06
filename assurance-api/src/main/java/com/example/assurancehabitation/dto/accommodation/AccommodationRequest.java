package com.example.assurancehabitation.dto.accommodation;

import com.example.assurancehabitation.entity.assurance.AccommodationType;
import jakarta.validation.constraints.*;

public record AccommodationRequest(
        @NotBlank(message = "Street is required")
        @Size(max = 150)
        String street,

        @NotBlank(message = "City is required")
        @Size(max = 100)
        String city,

        @NotBlank(message = "Country is required")
        @Size(max = 60)
        String country,

        @NotBlank(message = "Postal code is required")
        @Size(max = 10)
        String postalCode,

        @NotNull(message = "Accommodation type is required")
        AccommodationType type,

        @NotNull(message = "Surface area is required")
        @Positive(message = "Surface area must be positive")
        Integer surfaceArea,

        @NotNull(message = "Room count is required")
        @Positive(message = "Room count must be positive")
        Integer roomCount,

        @NotNull(message = "User ID is required")
        Long userId
) {}
