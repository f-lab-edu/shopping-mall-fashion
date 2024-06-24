package com.example.flab.soft.shoppingmallfashion.user.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.flab.soft.shoppingmallfashion.exception.UserException;
import com.example.flab.soft.shoppingmallfashion.user.controller.UserSignUpInfo;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class UserServiceTest {
    private final UserService userService;

    @Autowired
    public UserServiceTest(UserService userService, UserRepository userRepository) {
        this.userService = userService;
    }

    @BeforeEach
    void beforeEach() {
        userService.signUp(
                new UserSignUpInfo(
                        "duplicated",
                        "Correct1#",
                        "correctName",
                        "duplicated@gmail.com",
                        "01012345678",
                        "duplicated"
                ));
    }

    @DisplayName("로그인 아이디 중복시 예외 발생")
    @Test
    @Transactional
    public void cannotSignUpWithExistingUsername() {
        //로그인 아이디 중복
        assertThrows(UserException.class, () -> userService.signUp(
                new UserSignUpInfo(
                        "duplicated",
                        "Correct1#",
                        "correctName",
                        "notDuplicated@gmail.com",
                        "01011111111",
                        "notDuplicated"
                )
        ));
    }

    @DisplayName("이메일 중복시 예외 발생")
    @Test
    @Transactional
    public void cannotSignUpWithExistingEmail() {
        //닉네임 중복
        assertThrows(UserException.class, () -> userService.signUp(
                new UserSignUpInfo(
                        "notDuplicated",
                        "Correct1#",
                        "correctName",
                        "duplicated@gmail.com",
                        "01011111111",
                        "notDuplicated"
                )
        ));
    }

    @DisplayName("휴대폰 번호 중복시 예외 발생")
    @Test
    @Transactional
    public void cannotSignUpWithExistingCellphoneNumber() {
        assertThrows(UserException.class, () -> userService.signUp(
                new UserSignUpInfo(
                        "notDuplicated",
                        "Correct1#",
                        "correctName",
                        "notDuplicated@gmail.com",
                        "01012345678",
                        "notDuplicated"
                )
        ));
    }

    @DisplayName("닉네임 중복시 예외 발생")
    @Test
    @Transactional
    public void cannotSignUpWithExistingNickname() {
        assertThrows(UserException.class, () -> userService.signUp(
                new UserSignUpInfo(
                        "notDuplicated",
                        "Correct1#",
                        "correctName",
                        "notDuplicated@gmail.com",
                        "01011111111",
                        "duplicated"
                )
        ));
    }
}
