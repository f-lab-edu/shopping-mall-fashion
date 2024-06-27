package com.example.flab.soft.shoppingmallfashion.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.flab.soft.shoppingmallfashion.auth.Role;
import com.example.flab.soft.shoppingmallfashion.auth.RoleRepository;
import com.example.flab.soft.shoppingmallfashion.auth.UserRole;
import com.example.flab.soft.shoppingmallfashion.auth.UserRoleRepository;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.user.controller.UserSignUpRequest;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;
    private UserSignUpRequest validUserSignUpRequest;

    @BeforeEach
    void setUp() {
        validUserSignUpRequest = UserSignUpRequest.builder()
                .email("valid.email@example.com")
                .password("ValidPass1#")
                .realName("Valid Name")
                .cellphoneNumber("01012345678")
                .nickname("validNick")
                .build();
    }

    @Test
    void whenSignUpWithValidInfo_thenUserIsSaved() {
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .build();

        when(userRepository.save(any())).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userService.signUp(validUserSignUpRequest);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertThat(savedUser.getEmail()).isEqualTo(validUserSignUpRequest.getEmail());
        assertThat(savedUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(savedUser.getRealName()).isEqualTo(validUserSignUpRequest.getRealName());
        assertThat(savedUser.getCellphoneNumber()).isEqualTo(validUserSignUpRequest.getCellphoneNumber());
        assertThat(savedUser.getNickname()).isEqualTo(validUserSignUpRequest.getNickname());
        assertThat(savedUser.getCreatedAt()).isEqualTo(LocalDate.now());
    }

    @DisplayName("이메일 중복시 예외 발생")
    @Test
    void whenSignUpWithDuplicateEmail_thenThrowsException() {
        when(userRepository.existsByEmail(validUserSignUpRequest.getEmail())).thenReturn(true);

        ApiException exception = assertThrows(ApiException.class, () -> {
            userService.signUp(validUserSignUpRequest);
        });

        assertThat(exception.getError()).isEqualTo(ErrorEnum.EMAIL_DUPLICATED);
    }

    @DisplayName("전화번호 중복시 예외 발생")
    @Test
    void whenSignUpWithDuplicateCellphoneNumber_thenThrowsException() {
        when(userRepository.existsByCellphoneNumber(validUserSignUpRequest.getCellphoneNumber())).thenReturn(true);

        ApiException exception = assertThrows(ApiException.class, () -> {
            userService.signUp(validUserSignUpRequest);
        });

        assertThat(exception.getError()).isEqualTo(ErrorEnum.CELLPHONE_DUPLICATED);
    }

    @DisplayName("닉네임 중복시 예외 발생")
    @Test
    void whenSignUpWithDuplicateNickname_thenThrowsException() {
        when(userRepository.existsByNickname(validUserSignUpRequest.getNickname())).thenReturn(true);

        ApiException exception = assertThrows(ApiException.class, () -> {
            userService.signUp(validUserSignUpRequest);
        });

        assertThat(exception.getError()).isEqualTo(ErrorEnum.NICKNAME_DUPLICATED);
    }

    @DisplayName("회원가입시 유저 role 저장")
    @Test
    void whenSignUpWithValidInfo_thenUserRoleIsSaved() {
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .build();

        Role userRole = Role.builder()
                .id(1L)
                .authority("ROLE_USER")
                .build();

        when(userRepository.save(any())).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.findByAuthority("ROLE_USER")).thenReturn(userRole);

        userService.signUp(validUserSignUpRequest);

        ArgumentCaptor<UserRole> userRoleCaptor = ArgumentCaptor.forClass(UserRole.class);
        verify(userRoleRepository).save(userRoleCaptor.capture());
        UserRole savedUserRole = userRoleCaptor.getValue();

        assertThat(savedUserRole.getRole()).isEqualTo(userRole);
        assertThat(savedUserRole.getUserId()).isEqualTo(1L);
    }
}
