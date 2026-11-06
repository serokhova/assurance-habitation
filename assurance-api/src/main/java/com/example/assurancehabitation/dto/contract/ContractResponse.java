package com.example.assurancehabitation.dto.contract;

import com.example.assurancehabitation.entity.assurance.ContractFormula;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ContractResponse(
        Long id,
        LocalDate startDate,
        LocalDate endDate,
        ContractFormula formula,
        Long accommodationId,
        List<Long> coverageIds,
        LocalDateTime createdAt
) {}
