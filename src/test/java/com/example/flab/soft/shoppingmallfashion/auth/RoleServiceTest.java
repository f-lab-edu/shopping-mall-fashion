package com.example.flab.soft.shoppingmallfashion.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.flab.soft.shoppingmallfashion.auth.role.Authority;
import com.example.flab.soft.shoppingmallfashion.auth.role.Role;
import com.example.flab.soft.shoppingmallfashion.auth.role.RoleRepository;
import com.example.flab.soft.shoppingmallfashion.auth.role.UserRole;
import com.example.flab.soft.shoppingmallfashion.auth.role.UserRoleRepository;
import com.example.flab.soft.shoppingmallfashion.auth.role.RoleService;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleService roleService;

    @DisplayName("유저 role 저장")
    @Test
    void userRoleIsSaved() {
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .build();

        Role role = Role.builder()
                .id(1L)
                .authority(Authority.ROLE_USER)
                .build();

        when(roleRepository.findByAuthority(any())).thenReturn(role);

        roleService.save(user, Authority.ROLE_USER);

        ArgumentCaptor<UserRole> userRoleCaptor = ArgumentCaptor.forClass(UserRole.class);
        verify(userRoleRepository).save(userRoleCaptor.capture());
        UserRole savedUserRole = userRoleCaptor.getValue();

        assertThat(savedUserRole.getRole()).isEqualTo(role);
        assertThat(savedUserRole.getUserId()).isEqualTo(1L);
    }
}
