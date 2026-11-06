package com.example.assurancehabitation.controller;

import com.example.assurancehabitation.dto.contract.ContractRequest;
import com.example.assurancehabitation.dto.contract.ContractResponse;
import com.example.assurancehabitation.service.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contracts")
@Tag(name = "Contracts", description = "Home insurance contract management")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping
    @Operation(summary = "Create a contract")
    public ResponseEntity<ContractResponse> create(@Valid @RequestBody ContractRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contractService.create(request));
    }

    @GetMapping
    @Operation(summary = "List all contracts")
    public ResponseEntity<List<ContractResponse>> findAll() {
        return ResponseEntity.ok(contractService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a contract by ID")
    public ResponseEntity<ContractResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(contractService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a contract")
    public ResponseEntity<ContractResponse> update(@PathVariable Long id,
                                                    @Valid @RequestBody ContractRequest request) {
        return ResponseEntity.ok(contractService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a contract")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contractService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
