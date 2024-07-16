package com.example.flab.soft.shoppingmallfashion.store.repository;

import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CrewRepository extends JpaRepository<Crew, Long> {
    @Query("SELECT c FROM crews c " +
            "JOIN FETCH c.user u " +
            "JOIN FETCH c.store s " +
            "JOIN FETCH c.crewRoles cr " +
            "JOIN FETCH cr.roleEntity r " +
            "WHERE c.user.id = :userId")
    Optional<Crew> findCrewWithRolesByUserId(Long userId);
    boolean existsByUser(User user);
}
