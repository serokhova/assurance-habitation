package com.example.assurancehabitation.service;

import com.example.assurancehabitation.config.TestJmsConfig;
import com.example.assurancehabitation.dto.claim.ClaimRequest;
import com.example.assurancehabitation.dto.claim.ClaimResponse;
import com.example.assurancehabitation.entity.assurance.*;
import com.example.assurancehabitation.exception.ResourceNotFoundException;
import com.example.assurancehabitation.messaging.producer.JmsEventProducer;
import com.example.assurancehabitation.repository.AccommodationRepository;
import com.example.assurancehabitation.repository.ContractRepository;
import com.example.assurancehabitation.repository.CoverageRepository;
import com.example.assurancehabitation.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestJmsConfig.class)
@Transactional
class ClaimServiceTest {

    @Autowired
    private ClaimService claimService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private CoverageRepository coverageRepository;

    @Autowired
    private ContractRepository contractRepository;

    @MockitoBean
    private JmsEventProducer jmsEventProducer;

    private Long testUserId;
    private Long testContractId;
    private Long testCoverageId;

    @BeforeEach
    void setUp() {
        User user = new User("John Smith", "john.smith@example.com", "+1-555-4321");
        userRepository.save(user);
        testUserId = user.getId();

        Accommodation accommodation = new Accommodation();
        accommodation.setUser(user);
        accommodation.setType(AccommodationType.appartment);
        accommodation.setSurfaceArea(75);
        accommodation.setRoomCount(3);
        accommodation.setAddress(new Address("45 Elm Street", "Boston", "USA", "02108"));
        accommodationRepository.save(accommodation);

        Coverage coverage = new Coverage("WATER_DAMAGE", "Water leaks and flooding", 30.0);
        coverageRepository.save(coverage);
        testCoverageId = coverage.getId();

        Contract contract = new Contract();
        contract.setAccommodation(accommodation);
        contract.setFormula(ContractFormula.standard);
        contract.setStartDate(LocalDate.now());
        contract.setEndDate(LocalDate.now().plusYears(1));
        contract.setCoverages(List.of(coverage));
        contractRepository.save(contract);
        testContractId = contract.getId();
    }

    @Test
    void createClaim_shouldPersistAndPublishJmsEvent() {
        ClaimRequest request = new ClaimRequest(
                testUserId,
                testContractId,
                testCoverageId,
                LocalDate.now().minusDays(1),
                "Bathroom pipe burst causing ceiling leakage"
        );

        ClaimResponse response = claimService.create(request);

        assertThat(response.id()).isNotNull();
        assertThat(response.userId()).isEqualTo(testUserId);
        assertThat(response.contractId()).isEqualTo(testContractId);
        assertThat(response.coverageId()).isEqualTo(testCoverageId);
        assertThat(response.description()).isEqualTo("Bathroom pipe burst causing ceiling leakage");

        // Verify Event Message publication
        verify(jmsEventProducer).publish(eq("claim.created.queue"), eq("CLAIM_CREATED"), any());
    }

    @Test
    void findById_nonExisting_shouldThrowException() {
        assertThatThrownBy(() -> claimService.findById(88888L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("88888");
    }
}
