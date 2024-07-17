package com.example.flab.soft.shoppingmallfashion.auth;

import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetailsService.UserAuthService;
import com.example.flab.soft.shoppingmallfashion.store.repository.CrewRepository;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private CrewRepository crewRepository;
    @InjectMocks
    private UserAuthService userAuthService;

    @Test
    @DisplayName("존재하는 유저면 예외를 던진다")
    void loadUserByUsername_userDoesNotExist_throwsException() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userAuthService.loadUserByUsername("nonexistent@example.com");
        });

        assertEquals("nonexistent@example.com", exception.getMessage());

        verify(userRepository).findByEmail("nonexistent@example.com");
    }
}
