package com.example.assurancehabitation.repository;

import com.example.assurancehabitation.entity.assurance.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
