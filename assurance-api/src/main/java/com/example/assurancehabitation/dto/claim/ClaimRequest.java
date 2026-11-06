package com.example.assurancehabitation.dto.claim;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ClaimRequest(
        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Contract ID is required")
        Long contractId,

        @NotNull(message = "Coverage ID is required")
        Long coverageId,

        @NotNull(message = "Incident date is required")
        LocalDate incidentDate,

        String description
) {}
