package com.example.flab.soft.shoppingmallfashion.auth.repository;

import com.example.flab.soft.shoppingmallfashion.auth.domain.UserRole;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findAllByUserId(Long userId);
}
