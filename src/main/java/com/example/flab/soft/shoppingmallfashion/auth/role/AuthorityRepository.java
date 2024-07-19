package com.example.flab.soft.shoppingmallfashion.auth.role;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {
    @Query("""
        SELECT a
        FROM authorities a
        JOIN a.roleAuthorities ra
        JOIN ra.role r
        JOIN r.crewRoles cr
        JOIN cr.crew c
        WHERE c.id = :crewId
    """)
    List<AuthorityEntity> findAllByCrewId(Long crewId);
}
