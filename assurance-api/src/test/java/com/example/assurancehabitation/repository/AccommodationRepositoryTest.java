package com.example.assurancehabitation.repository;

import com.example.assurancehabitation.entity.assurance.Accommodation;
import com.example.assurancehabitation.entity.assurance.AccommodationType;
import com.example.assurancehabitation.entity.assurance.Address;
import com.example.assurancehabitation.entity.assurance.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccommodationRepositoryTest {

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndFindByUserId_shouldPersistEmbeddedAddressAndRelations() {
        // Given a policyholder
        User user = new User("Robert Davis", "robert.davis@example.com", "+1-555-8899");
        user = userRepository.save(user);

        // Given an accommodation with Embedded Address
        Accommodation accommodation = new Accommodation();
        accommodation.setUser(user);
        accommodation.setType(AccommodationType.house);
        accommodation.setSurfaceArea(150);
        accommodation.setRoomCount(6);
        accommodation.setAddress(new Address("12 Ocean Drive", "Miami", "USA", "33101"));

        accommodationRepository.save(accommodation);

        // When querying by user id
        List<Accommodation> accommodations = accommodationRepository.findByUserId(user.getId());

        // Then verify persistence and embedded value mapping
        assertThat(accommodations).hasSize(1);
        Accommodation saved = accommodations.get(0);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getType()).isEqualTo(AccommodationType.house);
        assertThat(saved.getSurfaceArea()).isEqualTo(150);
        assertThat(saved.getRoomCount()).isEqualTo(6);
        assertThat(saved.getAddress()).isNotNull();
        assertThat(saved.getAddress().getStreet()).isEqualTo("12 Ocean Drive");
        assertThat(saved.getAddress().getCity()).isEqualTo("Miami");
        assertThat(saved.getAddress().getPostalCode()).isEqualTo("33101");
        assertThat(saved.getCreatedAt()).isNotNull();
    }
}
