package com.example.assurancehabitation.service;

import com.example.assurancehabitation.dto.contract.ContractRequest;
import com.example.assurancehabitation.dto.contract.ContractResponse;
import com.example.assurancehabitation.entity.assurance.Accommodation;
import com.example.assurancehabitation.entity.assurance.Contract;
import com.example.assurancehabitation.entity.assurance.Coverage;
import com.example.assurancehabitation.exception.ResourceNotFoundException;
import com.example.assurancehabitation.messaging.producer.JmsEventProducer;
import com.example.assurancehabitation.repository.AccommodationRepository;
import com.example.assurancehabitation.repository.ContractRepository;
import com.example.assurancehabitation.repository.CoverageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ContractService {

    private static final String QUEUE_CONTRACT_CREATED = "contract.created.queue";

    private final ContractRepository contractRepository;
    private final AccommodationRepository accommodationRepository;
    private final CoverageRepository coverageRepository;
    private final JmsEventProducer jmsEventProducer;

    public ContractService(ContractRepository contractRepository,
                           AccommodationRepository accommodationRepository,
                           CoverageRepository coverageRepository,
                           JmsEventProducer jmsEventProducer) {
        this.contractRepository = contractRepository;
        this.accommodationRepository = accommodationRepository;
        this.coverageRepository = coverageRepository;
        this.jmsEventProducer = jmsEventProducer;
    }

    public ContractResponse create(ContractRequest request) {
        Accommodation accommodation = accommodationRepository.findById(request.accommodationId())
                .orElseThrow(() -> new ResourceNotFoundException("Accommodation not found with id: " + request.accommodationId()));

        List<Coverage> coverages = new ArrayList<>();
        if (request.coverageIds() != null) {
            coverages = coverageRepository.findAllById(request.coverageIds());
        }

        Contract contract = new Contract();
        contract.setStartDate(request.startDate());
        contract.setEndDate(request.endDate());
        contract.setFormula(request.formula());
        contract.setAccommodation(accommodation);
        contract.setCoverages(coverages);

        Contract saved = contractRepository.save(contract);

        // Event Message pattern: publish CONTRACT_CREATED
        jmsEventProducer.publish(QUEUE_CONTRACT_CREATED, "CONTRACT_CREATED", toResponse(saved));

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ContractResponse> findAll() {
        return contractRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ContractResponse findById(Long id) {
        return toResponse(findEntityById(id));
    }

    public ContractResponse update(Long id, ContractRequest request) {
        Contract contract = findEntityById(id);
        Accommodation accommodation = accommodationRepository.findById(request.accommodationId())
                .orElseThrow(() -> new ResourceNotFoundException("Accommodation not found with id: " + request.accommodationId()));

        List<Coverage> coverages = new ArrayList<>();
        if (request.coverageIds() != null) {
            coverages = coverageRepository.findAllById(request.coverageIds());
        }

        contract.setStartDate(request.startDate());
        contract.setEndDate(request.endDate());
        contract.setFormula(request.formula());
        contract.setAccommodation(accommodation);
        contract.setCoverages(coverages);

        return toResponse(contractRepository.save(contract));
    }

    public void delete(Long id) {
        if (!contractRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contract not found with id: " + id);
        }
        contractRepository.deleteById(id);
    }

    private Contract findEntityById(Long id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found with id: " + id));
    }

    private ContractResponse toResponse(Contract c) {
        return new ContractResponse(
                c.getId(),
                c.getStartDate(),
                c.getEndDate(),
                c.getFormula(),
                c.getAccommodation().getId(),
                c.getCoverages().stream().map(Coverage::getId).toList(),
                c.getCreatedAt()
        );
    }
}
