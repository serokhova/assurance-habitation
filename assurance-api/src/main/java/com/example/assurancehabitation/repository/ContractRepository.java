package com.example.assurancehabitation.repository;

import com.example.assurancehabitation.entity.assurance.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByAccommodationId(Long accommodationId);
}
