package com.example.flab.soft.shoppingmallfashion.auth.role;

import com.example.flab.soft.shoppingmallfashion.auth.role.Role;
import com.example.flab.soft.shoppingmallfashion.auth.role.Authority;
import com.example.flab.soft.shoppingmallfashion.auth.role.RoleRepository;
import com.example.flab.soft.shoppingmallfashion.auth.role.UserRole;
import com.example.flab.soft.shoppingmallfashion.auth.role.UserRoleRepository;
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
