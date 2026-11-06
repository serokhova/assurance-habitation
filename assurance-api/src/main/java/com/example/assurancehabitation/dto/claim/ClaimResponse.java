package com.example.assurancehabitation.dto.claim;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ClaimResponse(
        Long id,
        Long userId,
        Long contractId,
        Long coverageId,
        LocalDate incidentDate,
        String description,
        LocalDateTime createdAt
) {}
