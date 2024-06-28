package com.example.flab.soft.shoppingmallfashion.auth.service;

import com.example.flab.soft.shoppingmallfashion.auth.domain.Role;
import com.example.flab.soft.shoppingmallfashion.auth.domain.Authority;
import com.example.flab.soft.shoppingmallfashion.auth.repository.RoleRepository;
import com.example.flab.soft.shoppingmallfashion.auth.domain.UserRole;
import com.example.flab.soft.shoppingmallfashion.auth.repository.UserRoleRepository;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Transactional
    public void save(User user, Authority authority) {
        Role role = roleRepository.findByAuthority(authority);
        userRoleRepository.save(
                UserRole.builder()
                        .role(role)
                        .userId(user.getId())
                        .build());
    }
}
