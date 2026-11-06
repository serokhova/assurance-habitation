package com.example.assurancehabitation.dto.coverage;

import java.math.BigDecimal;

public record CoverageResponse(
        Long id,
        String code,
        String description,
        BigDecimal basePrice
) {}
