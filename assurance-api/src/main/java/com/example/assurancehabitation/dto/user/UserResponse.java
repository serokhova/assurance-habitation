package com.example.assurancehabitation.dto.user;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String name,
        String email,
        String phoneNumber,
        LocalDateTime createdAt
) {}
