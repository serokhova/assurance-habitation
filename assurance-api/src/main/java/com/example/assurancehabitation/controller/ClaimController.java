package com.example.assurancehabitation.controller;

import com.example.assurancehabitation.dto.claim.ClaimRequest;
import com.example.assurancehabitation.dto.claim.ClaimResponse;
import com.example.assurancehabitation.service.ClaimService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/claims")
@Tag(name = "Claims", description = "Insurance claim incident management")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping
    @Operation(summary = "File a claim")
    public ResponseEntity<ClaimResponse> create(@Valid @RequestBody ClaimRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(claimService.create(request));
    }

    @GetMapping
    @Operation(summary = "List all claims")
    public ResponseEntity<List<ClaimResponse>> findAll() {
        return ResponseEntity.ok(claimService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a claim by ID")
    public ResponseEntity<ClaimResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a claim")
    public ResponseEntity<ClaimResponse> update(@PathVariable Long id,
                                                 @Valid @RequestBody ClaimRequest request) {
        return ResponseEntity.ok(claimService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a claim")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        claimService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
