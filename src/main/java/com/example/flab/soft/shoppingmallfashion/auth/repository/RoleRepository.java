package com.example.flab.soft.shoppingmallfashion.auth.repository;

import com.example.flab.soft.shoppingmallfashion.auth.domain.Authority;
import com.example.flab.soft.shoppingmallfashion.auth.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByAuthority(Authority authority);
}
