package com.example.assurancehabitation.controller;

import com.example.assurancehabitation.dto.contract.ContractRequest;
import com.example.assurancehabitation.dto.contract.ContractResponse;
import com.example.assurancehabitation.entity.assurance.ContractFormula;
import com.example.assurancehabitation.exception.GlobalExceptionHandler;
import com.example.assurancehabitation.exception.ResourceNotFoundException;
import com.example.assurancehabitation.service.ContractService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContractController.class)
@Import(GlobalExceptionHandler.class)
class ContractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockitoBean
    private ContractService contractService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createContract_validRequest_shouldReturn201() throws Exception {
        ContractRequest request = new ContractRequest(
                LocalDate.now().plusDays(1),
                LocalDate.now().plusYears(1),
                ContractFormula.standard,
                1L,
                List.of(1L, 2L)
        );

        ContractResponse response = new ContractResponse(
                10L,
                request.startDate(),
                request.endDate(),
                request.formula(),
                1L,
                List.of(1L, 2L),
                LocalDateTime.now()
        );

        when(contractService.create(any())).thenReturn(response);

        mockMvc.perform(post("/contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.formula").value("standard"))
                .andExpect(jsonPath("$.accommodationId").value(1L));
    }

    @Test
    void createContract_missingAccommodationId_shouldReturn400() throws Exception {
        ContractRequest invalidRequest = new ContractRequest(
                LocalDate.now().plusDays(1),
                LocalDate.now().plusYears(1),
                ContractFormula.basic,
                null, // accommodationId is required
                List.of()
        );

        mockMvc.perform(post("/contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void getContract_existing_shouldReturn200() throws Exception {
        ContractResponse response = new ContractResponse(
                10L,
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                ContractFormula.premium,
                1L,
                List.of(1L),
                LocalDateTime.now()
        );

        when(contractService.findById(10L)).thenReturn(response);

        mockMvc.perform(get("/contracts/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.formula").value("premium"));
    }

    @Test
    void getContract_notFound_shouldReturn404() throws Exception {
        when(contractService.findById(999L))
                .thenThrow(new ResourceNotFoundException("Contract not found with id: 999"));

        mockMvc.perform(get("/contracts/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Contract not found with id: 999"));
    }

    @Test
    void deleteContract_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/contracts/10"))
                .andExpect(status().isNoContent());
    }
}
