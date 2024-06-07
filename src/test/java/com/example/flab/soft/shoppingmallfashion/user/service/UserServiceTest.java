package com.example.flab.soft.shoppingmallfashion.user.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.flab.soft.shoppingmallfashion.user.controller.UserSignUpDto;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import com.example.flab.soft.shoppingmallfashion.user.service.exception.badField.SignUpBadFieldException;
import com.example.flab.soft.shoppingmallfashion.user.service.exception.fieldConflict.SignUpFieldConflictException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class UserServiceTest {
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceTest(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }
    @DisplayName("아이디 형식이 맞지 않으면 예외 발생")
    @Test
    public void badField() {
        assertThrows(SignUpBadFieldException.class, () -> userService.signUp(
                new UserSignUpDto(
                        "test-1",
                        "bad",
                        "correctName",
                        "correct@gmail.com",
                        "01012345678",
                        "correctNickname"
                        )
        ));
        assertFalse(userRepository.existsByUserSigninInfo_SigninId("test-1"));
        assertFalse(userRepository.existsByEmail("correct@gmail.com"));

        assertDoesNotThrow(() -> {
            userService.signUp(
                    new UserSignUpDto(
                            "test-1",
                            "Correct1#",
                            "correctName",
                            "correct@gmail.com",
                            "01012345678",
                            "correctNickname"
                    )
            );
        });
    }

    @DisplayName("필드 중복시 예외 발생")
    @Test
    public void cannotSignUpWithExistingField() {
        assertDoesNotThrow(() -> userService.signUp(
                new UserSignUpDto(
                        "test-2",
                        "Correct1#",
                        "correctName",
                        "correct@gmail.com",
                        "01012345678",
                        "correctNickname"
                )
        ));

        //로그인 아이디 중복
        assertThrows(SignUpFieldConflictException.class, () -> userService.signUp(
                new UserSignUpDto(
                        "test-2",
                        "Correct1#",
                        "correctName",
                        "correct@gmail.com",
                        "01012345678",
                        "correctNickname2"
                )
        ));

        //닉네임 중복
        assertThrows(SignUpFieldConflictException.class, () -> userService.signUp(
                new UserSignUpDto(
                        "test-3",
                        "Correct1#",
                        "correctName",
                        "correct@gmail.com",
                        "01012345678",
                        "correctNickname"
                )
        ));
    }
}
