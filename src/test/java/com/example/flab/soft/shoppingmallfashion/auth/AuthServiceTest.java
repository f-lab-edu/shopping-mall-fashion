package com.example.flab.soft.shoppingmallfashion.auth;

import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
    @InjectMocks
    private AuthService authService;
    private User user;
    private UserRole userRole;
    private Role role;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .build();

        role = Role.builder()
                .id(1L)
                .authority("ROLE_USER")
                .build();

        userRole = UserRole.builder()
                .id(1L)
                .role(role)
                .userId(1L)
                .build();
    }

    @Test
    void loadUserByUsername_userExists_returnsUserDetails() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userRoleRepository.findAllByUserId(1L)).thenReturn(List.of(userRole));

        UserDetails userDetails = authService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER")));

        verify(userRepository).findByEmail("test@example.com");
        verify(userRoleRepository).findAllByUserId(1L);
    }

    @Test
    void loadUserByUsername_userDoesNotExist_throwsException() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            authService.loadUserByUsername("nonexistent@example.com");
        });

        assertEquals("nonexistent@example.com", exception.getMessage());

        verify(userRepository).findByEmail("nonexistent@example.com");
        verify(userRoleRepository, never()).findAllByUserId(anyLong());
    }
}
