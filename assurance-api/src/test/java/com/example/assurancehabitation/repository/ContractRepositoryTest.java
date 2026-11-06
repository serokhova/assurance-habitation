package com.example.assurancehabitation.repository;

import com.example.assurancehabitation.entity.assurance.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ContractRepositoryTest {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CoverageRepository coverageRepository;

    @Test
    void saveAndFindByAccommodationId_shouldPersistFormulaAndCoverages() {
        User user = userRepository.save(new User("Marcus Brody", "marcus@example.com", "+1-555-7766"));

        Accommodation accommodation = new Accommodation();
        accommodation.setUser(user);
        accommodation.setType(AccommodationType.appartment);
        accommodation.setSurfaceArea(80);
        accommodation.setRoomCount(3);
        accommodation.setAddress(new Address("88 Pine Street", "Chicago", "USA", "60601"));
        accommodation = accommodationRepository.save(accommodation);

        Coverage theft = coverageRepository.save(new Coverage("THEFT", "Theft and burglary protection", 25.0));
        Coverage liability = coverageRepository.save(new Coverage("LIABILITY", "Civil liability coverage", 15.0));

        Contract contract = new Contract();
        contract.setAccommodation(accommodation);
        contract.setFormula(ContractFormula.premium);
        contract.setStartDate(LocalDate.of(2026, 1, 1));
        contract.setEndDate(LocalDate.of(2027, 1, 1));
        contract.setCoverages(List.of(theft, liability));

        contractRepository.save(contract);

        List<Contract> results = contractRepository.findByAccommodationId(accommodation.getId());

        assertThat(results).hasSize(1);
        Contract saved = results.get(0);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getFormula()).isEqualTo(ContractFormula.premium);
        assertThat(saved.getCoverages()).hasSize(2);
    }
}
