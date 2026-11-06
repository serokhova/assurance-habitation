package com.example.assurancehabitation.dto.accommodation;

import com.example.assurancehabitation.entity.assurance.AccommodationType;
import java.time.LocalDateTime;

public record AccommodationResponse(
        Long id,
        String street,
        String city,
        String country,
        String postalCode,
        AccommodationType type,
        Integer surfaceArea,
        Integer roomCount,
        Long userId,
        LocalDateTime createdAt
) {}
