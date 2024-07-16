package com.example.flab.soft.shoppingmallfashion.auth.role;

import com.example.flab.soft.shoppingmallfashion.store.repository.Crew;
import com.example.flab.soft.shoppingmallfashion.store.repository.CrewRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    @Transactional
    public void assignRole(Crew crew, Role role) {
        RoleEntity roleEntity = roleRepository.findByRole(Role.STORE_MANAGER_BEFORE_APPROVAL);
        crew.addRole(CrewRole.builder()
                .crew(crew)
                .roleEntity(roleEntity)
                .build());
    }
}
