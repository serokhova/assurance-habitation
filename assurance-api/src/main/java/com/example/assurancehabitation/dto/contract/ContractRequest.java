package com.example.assurancehabitation.dto.contract;

import com.example.assurancehabitation.entity.assurance.ContractFormula;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record ContractRequest(
        @NotNull(message = "Start date is required")
        LocalDate startDate,

        @NotNull(message = "End date is required")
        LocalDate endDate,

        @NotNull(message = "Formula is required")
        ContractFormula formula,

        @NotNull(message = "Accommodation ID is required")
        Long accommodationId,

        List<Long> coverageIds
) {}
