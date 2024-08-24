package com.example.flab.soft.shoppingmallfashion.store.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CrewRoleRepository extends JpaRepository<CrewRole, Long> {
    @Query("SELECT cr FROM crew_roles cr "
            + "JOIN FETCH cr.roleEntity "
            + "JOIN FETCH cr.crew c "
            + "WHERE c.id = :crewId")
    List<CrewRole> findByCrewIdJoinFetch(Long crewId);
}
