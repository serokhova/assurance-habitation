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
class ClaimRepositoryTest {

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private CoverageRepository coverageRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Test
    void saveAndFindByUserIdOrderByIncidentDateDesc_shouldReturnSortedClaims() {
        User user = userRepository.save(new User("Sarah Connor", "sarah@example.com", "+1-555-9000"));

        Accommodation accommodation = new Accommodation();
        accommodation.setUser(user);
        accommodation.setType(AccommodationType.house);
        accommodation.setSurfaceArea(110);
        accommodation.setRoomCount(4);
        accommodation.setAddress(new Address("77 Highland Ave", "Austin", "USA", "73301"));
        accommodation = accommodationRepository.save(accommodation);

        Coverage fire = coverageRepository.save(new Coverage("FIRE", "Fire coverage", 40.0));

        Contract contract = new Contract();
        contract.setAccommodation(accommodation);
        contract.setFormula(ContractFormula.standard);
        contract.setStartDate(LocalDate.of(2026, 1, 1));
        contract.setEndDate(LocalDate.of(2027, 1, 1));
        contract.setCoverages(List.of(fire));
        contract = contractRepository.save(contract);

        Claim claim1 = new Claim();
        claim1.setUser(user);
        claim1.setContract(contract);
        claim1.setCoverage(fire);
        claim1.setIncidentDate(LocalDate.of(2026, 2, 10));
        claim1.setDescription("Small electrical fire in garage");
        claimRepository.save(claim1);

        Claim claim2 = new Claim();
        claim2.setUser(user);
        claim2.setContract(contract);
        claim2.setCoverage(fire);
        claim2.setIncidentDate(LocalDate.of(2026, 5, 20));
        claim2.setDescription("Kitchen stove smoke damage");
        claimRepository.save(claim2);

        List<Claim> claims = claimRepository.findByUserIdOrderByIncidentDateDesc(user.getId());

        assertThat(claims).hasSize(2);
        assertThat(claims.get(0).getIncidentDate()).isEqualTo(LocalDate.of(2026, 5, 20));
        assertThat(claims.get(1).getIncidentDate()).isEqualTo(LocalDate.of(2026, 2, 10));
    }
}
