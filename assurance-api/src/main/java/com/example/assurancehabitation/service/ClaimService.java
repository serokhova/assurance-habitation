package com.example.assurancehabitation.service;

import com.example.assurancehabitation.dto.claim.ClaimRequest;
import com.example.assurancehabitation.dto.claim.ClaimResponse;
import com.example.assurancehabitation.entity.assurance.Claim;
import com.example.assurancehabitation.entity.assurance.Contract;
import com.example.assurancehabitation.entity.assurance.Coverage;
import com.example.assurancehabitation.entity.assurance.User;
import com.example.assurancehabitation.exception.ResourceNotFoundException;
import com.example.assurancehabitation.messaging.producer.JmsEventProducer;
import com.example.assurancehabitation.repository.ClaimRepository;
import com.example.assurancehabitation.repository.ContractRepository;
import com.example.assurancehabitation.repository.CoverageRepository;
import com.example.assurancehabitation.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClaimService {

    private static final String QUEUE_CLAIM_CREATED = "claim.created.queue";

    private final ClaimRepository claimRepository;
    private final UserRepository userRepository;
    private final ContractRepository contractRepository;
    private final CoverageRepository coverageRepository;
    private final JmsEventProducer jmsEventProducer;

    public ClaimService(ClaimRepository claimRepository,
                        UserRepository userRepository,
                        ContractRepository contractRepository,
                        CoverageRepository coverageRepository,
                        JmsEventProducer jmsEventProducer) {
        this.claimRepository = claimRepository;
        this.userRepository = userRepository;
        this.contractRepository = contractRepository;
        this.coverageRepository = coverageRepository;
        this.jmsEventProducer = jmsEventProducer;
    }

    public ClaimResponse create(ClaimRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.userId()));
        Contract contract = contractRepository.findById(request.contractId())
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found with id: " + request.contractId()));
        Coverage coverage = coverageRepository.findById(request.coverageId())
                .orElseThrow(() -> new ResourceNotFoundException("Coverage not found with id: " + request.coverageId()));

        Claim claim = new Claim();
        claim.setUser(user);
        claim.setContract(contract);
        claim.setCoverage(coverage);
        claim.setIncidentDate(request.incidentDate());
        claim.setDescription(request.description());

        Claim saved = claimRepository.save(claim);

        // Event Message pattern: publish CLAIM_CREATED
        jmsEventProducer.publish(QUEUE_CLAIM_CREATED, "CLAIM_CREATED", toResponse(saved));

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ClaimResponse> findAll() {
        return claimRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ClaimResponse findById(Long id) {
        return toResponse(findEntityById(id));
    }

    public ClaimResponse update(Long id, ClaimRequest request) {
        Claim claim = findEntityById(id);
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.userId()));
        Contract contract = contractRepository.findById(request.contractId())
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found with id: " + request.contractId()));
        Coverage coverage = coverageRepository.findById(request.coverageId())
                .orElseThrow(() -> new ResourceNotFoundException("Coverage not found with id: " + request.coverageId()));

        claim.setUser(user);
        claim.setContract(contract);
        claim.setCoverage(coverage);
        claim.setIncidentDate(request.incidentDate());
        claim.setDescription(request.description());

        return toResponse(claimRepository.save(claim));
    }

    public void delete(Long id) {
        if (!claimRepository.existsById(id)) {
            throw new ResourceNotFoundException("Claim not found with id: " + id);
        }
        claimRepository.deleteById(id);
    }

    private Claim findEntityById(Long id) {
        return claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + id));
    }

    private ClaimResponse toResponse(Claim c) {
        return new ClaimResponse(
                c.getId(),
                c.getUser().getId(),
                c.getContract().getId(),
                c.getCoverage().getId(),
                c.getIncidentDate(),
                c.getDescription(),
                c.getCreatedAt()
        );
    }
}
