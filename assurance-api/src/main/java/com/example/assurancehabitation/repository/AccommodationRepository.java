package com.example.assurancehabitation.repository;

import com.example.assurancehabitation.entity.assurance.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    List<Accommodation> findByUserId(Long userId);
}
