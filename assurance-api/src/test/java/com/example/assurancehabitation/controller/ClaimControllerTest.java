package com.example.assurancehabitation.controller;

import com.example.assurancehabitation.dto.claim.ClaimRequest;
import com.example.assurancehabitation.dto.claim.ClaimResponse;
import com.example.assurancehabitation.exception.GlobalExceptionHandler;
import com.example.assurancehabitation.exception.ResourceNotFoundException;
import com.example.assurancehabitation.service.ClaimService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClaimController.class)
@Import(GlobalExceptionHandler.class)
class ClaimControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockitoBean
    private ClaimService claimService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createClaim_validRequest_shouldReturn201() throws Exception {
        ClaimRequest request = new ClaimRequest(
                1L,
                1L,
                1L,
                LocalDate.now().minusDays(2),
                "Water leak from the upstairs bathroom ceiling"
        );

        ClaimResponse response = new ClaimResponse(
                5L,
                1L,
                1L,
                1L,
                request.incidentDate(),
                request.description(),
                LocalDateTime.now()
        );

        when(claimService.create(any())).thenReturn(response);

        mockMvc.perform(post("/claims")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.description").value("Water leak from the upstairs bathroom ceiling"));
    }

    @Test
    void createClaim_missingIncidentDate_shouldReturn400() throws Exception {
        ClaimRequest invalidRequest = new ClaimRequest(
                1L,
                1L,
                1L,
                null, // missing required date
                "Water leak"
        );

        mockMvc.perform(post("/claims")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void getClaim_notFound_shouldReturn404() throws Exception {
        when(claimService.findById(999L))
                .thenThrow(new ResourceNotFoundException("Claim not found with id: 999"));

        mockMvc.perform(get("/claims/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
