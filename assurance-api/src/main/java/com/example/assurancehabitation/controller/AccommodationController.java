package com.example.assurancehabitation.controller;

import com.example.assurancehabitation.dto.accommodation.AccommodationRequest;
import com.example.assurancehabitation.dto.accommodation.AccommodationResponse;
import com.example.assurancehabitation.service.AccommodationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accommodations")
@Tag(name = "Accommodations", description = "Insured property management")
public class AccommodationController {

    private final AccommodationService accommodationService;

    public AccommodationController(AccommodationService accommodationService) {
        this.accommodationService = accommodationService;
    }

    @PostMapping
    @Operation(summary = "Create an accommodation")
    public ResponseEntity<AccommodationResponse> create(@Valid @RequestBody AccommodationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accommodationService.create(request));
    }

    @GetMapping
    @Operation(summary = "List all accommodations")
    public ResponseEntity<List<AccommodationResponse>> findAll() {
        return ResponseEntity.ok(accommodationService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an accommodation by ID")
    public ResponseEntity<AccommodationResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(accommodationService.findById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "List accommodations of a policyholder")
    public ResponseEntity<List<AccommodationResponse>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(accommodationService.findByUserId(userId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an accommodation")
    public ResponseEntity<AccommodationResponse> update(@PathVariable Long id,
                                                         @Valid @RequestBody AccommodationRequest request) {
        return ResponseEntity.ok(accommodationService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an accommodation")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accommodationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
