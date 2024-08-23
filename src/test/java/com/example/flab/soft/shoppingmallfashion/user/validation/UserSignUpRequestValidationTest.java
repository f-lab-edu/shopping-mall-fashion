package com.example.flab.soft.shoppingmallfashion.user.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.flab.soft.shoppingmallfashion.user.controller.UserSignUpRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserSignUpRequestValidationTest {
    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("올바른 형식 제공시 에러가 발생하지 않는다.")
    public void testValidUserSignUpInfo() {
        UserSignUpRequest user = UserSignUpRequest.builder()
                .email("john.doe@example.com")
                .password("Valid1#Password")
                .realName("John Doe")
                .cellphoneNumber("01012345678")
                .nickname("validNickname")
                .build();

        Set<ConstraintViolation<UserSignUpRequest>> violations = validator.validate(user);
        assertEquals(0, violations.size());
    }

    @Test
    @DisplayName("허용되지 않는 이메일 형식 제공시 에러 발생")
    public void testInvalidEmail() {
        UserSignUpRequest user = UserSignUpRequest.builder()
                .email("john.doe@com") // Invalid email
                .password("Valid1#Password")
                .realName("John Doe")
                .cellphoneNumber("01012345678")
                .nickname("validNickname")
                .build();

        Set<ConstraintViolation<UserSignUpRequest>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("허용되지 않는 이메일 형식입니다.", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("허용되지 않는 비밀번호 형식 제공시 에러 발생")
    public void testInvalidPassword() {
        UserSignUpRequest user = UserSignUpRequest.builder()
                .email("john.doe@example.com")
                .password("password") // Invalid password
                .realName("John Doe")
                .cellphoneNumber("01012345678")
                .nickname("validNickname")
                .build();

        Set<ConstraintViolation<UserSignUpRequest>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("허용되지 않는 비밀번호 형식입니다.", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("허용되지 않는 전화번호 형식 제공시 에러 발생")
    public void testInvalidCellphoneNumber() {
        UserSignUpRequest user = UserSignUpRequest.builder()
                .email("john.doe@example.com")
                .password("Valid1#Password")
                .realName("John Doe")
                .cellphoneNumber("0112345678") // Invalid phone number
                .nickname("validNickname")
                .build();

        Set<ConstraintViolation<UserSignUpRequest>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("허용되지 않는 전화번호 형식입니다.", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("허용되지 않는 닉네임 형식 제공시 에러 발생")
    public void testInvalidNickname() {
        UserSignUpRequest user = UserSignUpRequest.builder()
                .email("john.doe@example.com")
                .password("Valid1#Password")
                .realName("John Doe")
                .cellphoneNumber("01012345678")
                .nickname("a") // Invalid nickname
                .build();

        Set<ConstraintViolation<UserSignUpRequest>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("닉네임의 길이는 2자 이상 16자 이하입니다.", violations.iterator().next().getMessage());
    }
}
