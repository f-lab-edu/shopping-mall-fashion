package com.example.flab.soft.shoppingmallfashion.store.service;

import com.example.flab.soft.shoppingmallfashion.auth.role.Role;
import com.example.flab.soft.shoppingmallfashion.auth.role.RoleEntity;
import com.example.flab.soft.shoppingmallfashion.auth.role.RoleRepository;
import com.example.flab.soft.shoppingmallfashion.auth.role.RoleService;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.store.repository.Crew;
import com.example.flab.soft.shoppingmallfashion.store.repository.CrewRepository;
import com.example.flab.soft.shoppingmallfashion.store.repository.CrewRole;
import com.example.flab.soft.shoppingmallfashion.store.repository.Store;
import com.example.flab.soft.shoppingmallfashion.store.repository.StoreRepository;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CrewService {
    private final RoleService roleService;
    private final CrewRepository crewRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addManager(Store store, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ApiException(ErrorEnum.INVALID_REQUEST));

        Crew crew = crewRepository.save(Crew.builder()
                .user(user).store(store).build());
        roleService.assignRole(crew, Role.STORE_MANAGER_BEFORE_APPROVAL);
    }
}
