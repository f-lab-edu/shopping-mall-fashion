package com.example.flab.soft.shoppingmallfashion.store;

import static org.assertj.core.api.Assertions.*;

import com.example.flab.soft.shoppingmallfashion.auth.role.Role;
import com.example.flab.soft.shoppingmallfashion.auth.role.RoleRepository;
import com.example.flab.soft.shoppingmallfashion.store.controller.CrewSignUpRequest;
import com.example.flab.soft.shoppingmallfashion.store.repository.Crew;
import com.example.flab.soft.shoppingmallfashion.store.repository.CrewRepository;
import com.example.flab.soft.shoppingmallfashion.store.repository.CrewRole;
import com.example.flab.soft.shoppingmallfashion.store.repository.CrewRoleRepository;
import com.example.flab.soft.shoppingmallfashion.store.repository.Store;
import com.example.flab.soft.shoppingmallfashion.store.repository.StoreRepository;
import com.example.flab.soft.shoppingmallfashion.store.service.CrewBriefInfo;
import com.example.flab.soft.shoppingmallfashion.store.service.CrewService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CrewIntegrationTest {
    @Autowired
    private CrewRepository crewRepository;
    @Autowired
    private CrewService crewService;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private CrewRoleRepository crewRoleRepository;
    @Autowired
    private RoleRepository roleRepository;

    Store store;
    Crew crew;
    List<CrewRole> crewRoles = new ArrayList<>();
    private static final String STORE_NAME = "store name";
    @BeforeEach
    void setUp() {
        store = storeRepository.save(Store.builder()
                .name(STORE_NAME)
                .logo("logo")
                .businessRegistrationNumber("3333333333")
                .managerId(1L)
                .build());
    }

    @AfterEach
    void tearDown() {
        crewRoleRepository.deleteAll(crewRoles);
        crewRepository.delete(crew);
        storeRepository.delete(store);
    }

    @Test
    @DisplayName("첫 직원 등록, 미승인 상태여야 한다.")
    void addCrew() {
        //given
        CrewSignUpRequest crewSignUpRequest = CrewSignUpRequest.builder()
                .name("name")
                .email("email@email.com")
                .cellphoneNumber("01034356789")
                .password("password")
                .build();
        long crewCountBefore = crewRepository.count();
        //when
        CrewBriefInfo crewBriefInfo = crewService.addCrew(crewSignUpRequest, store.getId());
        //then
        crew = crewRepository.findById(crewBriefInfo.getId()).get();
        assertThat(crewRepository.count()).isEqualTo(crewCountBefore + 1);
        assertThat(crew.getApproved()).isFalse();
    }

    @Test
    @DisplayName("직원 승인")
    void approveCrew() {
        //given
        crew = crewRepository.save(Crew.builder()
                .name("name")
                .email("email@email.com")
                .password("password")
                .cellphoneNumber("01034356789")
                .store(store)
                .build());
        //when
        CrewBriefInfo crewBriefInfo = crewService.approve(crew.getId());
        //then
        crew = crewRepository.findById(crewBriefInfo.getId()).get();
        assertThat(crew.isApproved()).isTrue();
    }

    @Test
    @DisplayName("직원에게 권한 부여")
    void updateCrewRoles() {
        //given
        crew = crewRepository.save(Crew.builder()
                .name("name")
                .email("email@email.com")
                .password("password")
                .cellphoneNumber("01034356789")
                .store(store)
                .build());
        //when
        CrewBriefInfo crewBriefInfo = crewService.updateRoles(crew.getId(),
                List.of(Role.CREW_MANAGER, Role.STORE_MANAGER));
        //then
        crewRoles = crewRoleRepository.findByCrewIdJoinFetch(crew.getId());
        assertThat(crewRoles.size()).isEqualTo(2);
        assertThat(crewBriefInfo.getRoles().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("새로운 권한 추가, 해제된 권한 삭제")
    void addNewRole_deleteRoleNotInUpdatedList() {
        //given
        crew = crewRepository.save(Crew.builder()
                .name("name")
                .email("email@email.com")
                .password("password")
                .cellphoneNumber("01034356789")
                .store(store)
                .build());
        crewRoleRepository.save(CrewRole.builder()
                .roleEntity(roleRepository.findByRole(Role.ITEM_MANAGER))
                .crew(crew)
                .build());
        //when
        crewService.updateRoles(crew.getId(),
                List.of(Role.CREW_MANAGER, Role.STORE_MANAGER));
        //then
        crewRoles = crewRoleRepository.findByCrewIdJoinFetch(crew.getId());
        List<Role> roles = crewRoles.stream()
                .map(CrewRole::getRole)
                .toList();
        assertThat(roles.size()).isEqualTo(2);
        assertThat(roles).doesNotContain(Role.ITEM_MANAGER);
        assertThat(roles).contains(Role.STORE_MANAGER);
    }
}
