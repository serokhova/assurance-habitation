package com.example.assurancehabitation.repository;

import com.example.assurancehabitation.entity.assurance.Coverage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoverageRepository extends JpaRepository<Coverage, Long> {
}
