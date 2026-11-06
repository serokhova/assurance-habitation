package com.example.assurancehabitation.service;

import com.example.assurancehabitation.config.TestJmsConfig;
import com.example.assurancehabitation.dto.contract.ContractRequest;
import com.example.assurancehabitation.dto.contract.ContractResponse;
import com.example.assurancehabitation.entity.assurance.*;
import com.example.assurancehabitation.exception.ResourceNotFoundException;
import com.example.assurancehabitation.messaging.producer.JmsEventProducer;
import com.example.assurancehabitation.repository.AccommodationRepository;
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
class ContractServiceTest {

    @Autowired
    private ContractService contractService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private CoverageRepository coverageRepository;

    @MockitoBean
    private JmsEventProducer jmsEventProducer;

    private Long testAccommodationId;
    private Long testCoverageId;

    @BeforeEach
    void setUp() {
        User user = new User("Jane Doe", "jane.doe@example.com", "+1-555-1122");
        userRepository.save(user);

        Accommodation accommodation = new Accommodation();
        accommodation.setUser(user);
        accommodation.setType(AccommodationType.house);
        accommodation.setSurfaceArea(120);
        accommodation.setRoomCount(5);
        accommodation.setAddress(new Address("100 Main St", "Springfield", "USA", "12345"));
        accommodationRepository.save(accommodation);
        testAccommodationId = accommodation.getId();

        Coverage coverage = new Coverage("FIRE", "Fire and explosion protection", 45.0);
        coverageRepository.save(coverage);
        testCoverageId = coverage.getId();
    }

    @Test
    void createContract_shouldPersistAndPublishJmsEvent() {
        ContractRequest request = new ContractRequest(
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                ContractFormula.premium,
                testAccommodationId,
                List.of(testCoverageId)
        );

        ContractResponse response = contractService.create(request);

        assertThat(response.id()).isNotNull();
        assertThat(response.formula()).isEqualTo(ContractFormula.premium);
        assertThat(response.accommodationId()).isEqualTo(testAccommodationId);
        assertThat(response.coverageIds()).contains(testCoverageId);

        // Verify Event Message publication to Artemis queue
        verify(jmsEventProducer).publish(eq("contract.created.queue"), eq("CONTRACT_CREATED"), any());
    }

    @Test
    void findById_nonExisting_shouldThrowResourceNotFoundException() {
        assertThatThrownBy(() -> contractService.findById(99999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99999");
    }
}
