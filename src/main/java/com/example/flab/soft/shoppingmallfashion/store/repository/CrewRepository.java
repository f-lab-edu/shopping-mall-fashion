package com.example.flab.soft.shoppingmallfashion.store.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewRepository extends JpaRepository<Crew, Long> {
    Optional<Crew> findByEmail(String email);
}
