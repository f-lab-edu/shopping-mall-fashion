package com.example.flab.soft.shoppingmallfashion.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.user.controller.UserSignUpInfo;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;
    private UserSignUpInfo validUserSignUpInfo;

    @BeforeEach
    void setUp() {
        validUserSignUpInfo = UserSignUpInfo.builder()
                .username("validuser")
                .password("ValidPass1#")
                .realName("Valid Name")
                .email("valid.email@example.com")
                .cellphoneNumber("01012345678")
                .nickname("validNick")
                .build();
    }

    @Test
    void whenSignUpWithValidInfo_thenUserIsSaved() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userService.signUp(validUserSignUpInfo);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertThat(savedUser.getUsername()).isEqualTo(validUserSignUpInfo.getUsername());
        assertThat(savedUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(savedUser.getRealName()).isEqualTo(validUserSignUpInfo.getRealName());
        assertThat(savedUser.getEmail()).isEqualTo(validUserSignUpInfo.getEmail());
        assertThat(savedUser.getCellphoneNumber()).isEqualTo(validUserSignUpInfo.getCellphoneNumber());
        assertThat(savedUser.getNickname()).isEqualTo(validUserSignUpInfo.getNickname());
        assertThat(savedUser.getCreatedAt()).isEqualTo(LocalDate.now());
    }
    @DisplayName("아이디 중복시 예외 발생")
    @Test
    void whenSignUpWithDuplicateUsername_thenThrowsException() {
        when(userRepository.existsByUsername(validUserSignUpInfo.getUsername())).thenReturn(true);

        ApiException exception = assertThrows(ApiException.class, () -> {
            userService.signUp(validUserSignUpInfo);
        });

        assertThat(exception.getError()).isEqualTo(ErrorEnum.USERNAME_DUPLICATED);
    }
    @DisplayName("이메일 중복시 예외 발생")
    @Test
    void whenSignUpWithDuplicateEmail_thenThrowsException() {
        when(userRepository.existsByEmail(validUserSignUpInfo.getEmail())).thenReturn(true);

        ApiException exception = assertThrows(ApiException.class, () -> {
            userService.signUp(validUserSignUpInfo);
        });

        assertThat(exception.getError()).isEqualTo(ErrorEnum.EMAIL_DUPLICATED);
    }
    @DisplayName("전화번호 중복시 예외 발생")
    @Test
    void whenSignUpWithDuplicateCellphoneNumber_thenThrowsException() {
        when(userRepository.existsByCellphoneNumber(validUserSignUpInfo.getCellphoneNumber())).thenReturn(true);

        ApiException exception = assertThrows(ApiException.class, () -> {
            userService.signUp(validUserSignUpInfo);
        });

        assertThat(exception.getError()).isEqualTo(ErrorEnum.CELLPHONE_DUPLICATED);
    }
    @DisplayName("닉네임 중복시 예외 발생")
    @Test
    void whenSignUpWithDuplicateNickname_thenThrowsException() {
        when(userRepository.existsByNickname(validUserSignUpInfo.getNickname())).thenReturn(true);

        ApiException exception = assertThrows(ApiException.class, () -> {
            userService.signUp(validUserSignUpInfo);
        });

        assertThat(exception.getError()).isEqualTo(ErrorEnum.NICKNAME_DUPLICATED);
    }
    @DisplayName("일치하는 로그인 정보 제공시 유저 정보 제공")
    @Test
    void whenLoadUserByUsernameWithValidUsername_thenReturnsUserDetails() {
        User user = User.builder()
                .username("validUser")
                .password("encodedPassword")
                .build();
        when(userRepository.findByUsername("validUser")).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("validUser");

        assertThat(userDetails.getUsername()).isEqualTo("validUser");
        assertThat(userDetails.getPassword()).isEqualTo("encodedPassword");
    }
    @DisplayName("존재하지 않은 로그인 정보 제공시 인증 오류")
    @Test
    void whenLoadUserByUsernameWithInvalidUsername_thenThrowsException() {
        when(userRepository.findByUsername("invalidUser")).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> {
            userService.loadUserByUsername("invalidUser");
        });

        assertThat(exception.getError()).isEqualTo(ErrorEnum.AUTHENTICATION_FAILED);
    }
}
