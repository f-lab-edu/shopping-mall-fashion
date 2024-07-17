package com.example.flab.soft.shoppingmallfashion.store.repository;

import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewRepository extends JpaRepository<Crew, Long> {
    Optional<Crew> findByUserId(Long userId);
    boolean existsByUser(User user);
}
