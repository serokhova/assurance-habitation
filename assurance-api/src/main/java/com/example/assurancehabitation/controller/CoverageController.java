package com.example.assurancehabitation.controller;

import com.example.assurancehabitation.dto.coverage.CoverageRequest;
import com.example.assurancehabitation.dto.coverage.CoverageResponse;
import com.example.assurancehabitation.service.CoverageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coverages")
@Tag(name = "Coverages", description = "Insurance coverage catalog")
public class CoverageController {

    private final CoverageService coverageService;

    public CoverageController(CoverageService coverageService) {
        this.coverageService = coverageService;
    }

    @PostMapping
    @Operation(summary = "Create a coverage")
    public ResponseEntity<CoverageResponse> create(@Valid @RequestBody CoverageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(coverageService.create(request));
    }

    @GetMapping
    @Operation(summary = "List all coverages")
    public ResponseEntity<List<CoverageResponse>> findAll() {
        return ResponseEntity.ok(coverageService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a coverage by ID")
    public ResponseEntity<CoverageResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(coverageService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a coverage")
    public ResponseEntity<CoverageResponse> update(@PathVariable Long id,
                                                    @Valid @RequestBody CoverageRequest request) {
        return ResponseEntity.ok(coverageService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a coverage")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        coverageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
