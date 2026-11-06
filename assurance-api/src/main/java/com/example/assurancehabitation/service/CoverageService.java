package com.example.assurancehabitation.service;

import com.example.assurancehabitation.dto.coverage.CoverageRequest;
import com.example.assurancehabitation.dto.coverage.CoverageResponse;
import com.example.assurancehabitation.entity.assurance.Coverage;
import com.example.assurancehabitation.exception.ResourceNotFoundException;
import com.example.assurancehabitation.repository.CoverageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CoverageService {

    private final CoverageRepository coverageRepository;

    public CoverageService(CoverageRepository coverageRepository) {
        this.coverageRepository = coverageRepository;
    }

    public CoverageResponse create(CoverageRequest request) {
        Coverage coverage = new Coverage();
        coverage.setCode(request.code());
        coverage.setDescription(request.description());
        coverage.setBasePrice(request.basePrice());
        return toResponse(coverageRepository.save(coverage));
    }

    @Transactional(readOnly = true)
    public List<CoverageResponse> findAll() {
        return coverageRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CoverageResponse findById(Long id) {
        return toResponse(findEntityById(id));
    }

    public CoverageResponse update(Long id, CoverageRequest request) {
        Coverage coverage = findEntityById(id);
        coverage.setCode(request.code());
        coverage.setDescription(request.description());
        coverage.setBasePrice(request.basePrice());
        return toResponse(coverageRepository.save(coverage));
    }

    public void delete(Long id) {
        if (!coverageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Coverage not found with id: " + id);
        }
        coverageRepository.deleteById(id);
    }

    private Coverage findEntityById(Long id) {
        return coverageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coverage not found with id: " + id));
    }

    public CoverageResponse toResponse(Coverage c) {
        return new CoverageResponse(c.getId(), c.getCode(), c.getDescription(), c.getBasePrice());
    }
}
