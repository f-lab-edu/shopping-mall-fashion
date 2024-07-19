package com.example.flab.soft.shoppingmallfashion.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.user.controller.UserSignUpRequest;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import java.util.Optional;
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

    @DisplayName("올바른 정보 입력시 유저 저장")
    @Test
    void whenSignUpWithValidInfo_thenUserIsSaved() {
        User user = User.builder()
                .email("valid.email@example.com")
                .password("ValidPass1#")
                .realName("Valid Name")
                .cellphoneNumber("01012345678")
                .nickname("validNick")
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
        assertThat(savedUser.getEmail()).isEqualTo("valid.email@example.com");
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

    @DisplayName("현재 비밀번호 잘못 입력 예외 발생")
    @Test
    void whenChangePasswordWithWrongCurrentPassword_thenThrowsException() {
        when(userRepository.findById(any())).thenReturn(
                Optional.ofNullable(User.builder()
                        .email("valid.email@example.com")
                        .password("ValidPass1#")
                        .realName("Valid Name")
                        .cellphoneNumber("01012345678")
                        .nickname("validNick")
                        .build()));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        ApiException exception = assertThrows(ApiException.class, () -> {
            userService.changePassword(1L, "CurrentPassword", "NewPassword");
        });
        assertThat(exception.getError()).isEqualTo(ErrorEnum.WRONG_PASSWORD);
    }
}
