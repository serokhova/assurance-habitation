package com.example.assurancehabitation.repository;

import com.example.assurancehabitation.entity.assurance.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByUserIdOrderByIncidentDateDesc(Long userId);
    List<Claim> findByContractId(Long contractId);
}
