package com.example.assurancehabitation.dto.coverage;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CoverageRequest(
        @NotBlank(message = "Code is required")
        @Size(max = 30)
        String code,

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Base price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Base price must be positive")
        BigDecimal basePrice
) {}
