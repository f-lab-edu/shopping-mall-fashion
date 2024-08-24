package com.example.flab.soft.shoppingmallfashion.store.service;

import com.example.flab.soft.shoppingmallfashion.auth.role.Role;
import com.example.flab.soft.shoppingmallfashion.auth.role.RoleRepository;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.store.controller.CrewSignUpRequest;
import com.example.flab.soft.shoppingmallfashion.store.repository.Crew;
import com.example.flab.soft.shoppingmallfashion.store.repository.CrewRepository;
import com.example.flab.soft.shoppingmallfashion.store.repository.CrewRole;
import com.example.flab.soft.shoppingmallfashion.store.repository.CrewRoleRepository;
import com.example.flab.soft.shoppingmallfashion.store.repository.Store;
import com.example.flab.soft.shoppingmallfashion.store.repository.StoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CrewService {
    private final StoreRepository storeRepository;
    private final CrewRepository crewRepository;
    private final CrewRoleRepository crewRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public CrewBriefInfo addCrew(CrewSignUpRequest crewSignUpRequest, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ApiException(ErrorEnum.STORE_NOT_FOUND));

        Crew crew = crewRepository.save(Crew.builder()
                .email(crewSignUpRequest.getEmail())
                .password(passwordEncoder.encode(crewSignUpRequest.getPassword()))
                .cellphoneNumber(crewSignUpRequest.getCellphoneNumber())
                .name(crewSignUpRequest.getName())
                .store(store)
                .build());
        return CrewBriefInfo.builder().crew(crew).build();
    }

    @Transactional
    public CrewBriefInfo approve(Long crewId) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_CREW_ID));
        crew.beApproved();
        return CrewBriefInfo.builder().crew(crew).build();
    }

    @Transactional
    public CrewBriefInfo patchRoles(Long crewId, List<Role> patchRoles) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_CREW_ID));
        List<CrewRole> currentCrewRoles = crewRoleRepository.findByCrewIdJoinFetch(crewId);
        List<Role> currentRoles = currentCrewRoles.stream()
                .map(CrewRole::getRole)
                .toList();

        currentCrewRoles.stream()
                .filter(currentCrewRole -> !patchRoles.contains(currentCrewRole.getRole()))
                .forEach(crewRoleRepository::delete);

        patchRoles.stream()
                .filter(patchRole -> !currentRoles.contains(patchRole))
                .forEach(patchRole -> crewRoleRepository.save(
                        CrewRole.builder()
                                .roleEntity(roleRepository.findByRole(patchRole))
                                .crew(crew)
                                .build()));
        return CrewBriefInfo.builder()
                .crew(crew)
                .roles(crew.getCrewRoles().stream()
                        .map(CrewRole::getRole)
                        .toList())
                .build();
    }
}
