package com.example.flab.soft.shoppingmallfashion.user.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserFieldRegexTest {
    @DisplayName("로그인 아이디 형식 확인")
    @Test
    public void check_id() {
        assertThat(UserFieldType.ID.matches("a")).isFalse();
        assertThat(UserFieldType.ID.matches("123456")).isFalse();
        assertThat(UserFieldType.ID.matches("ABC1234")).isFalse();
        assertThat(UserFieldType.ID.matches("abc###")).isFalse();
        assertThat(UserFieldType.ID.matches("-----1")).isFalse();

        assertThat(UserFieldType.ID.matches("iuduf")).isTrue();
        assertThat(UserFieldType.ID.matches("ank123")).isTrue();
        assertThat(UserFieldType.ID.matches("aaaank123")).isTrue();
        assertThat(UserFieldType.ID.matches("abc---")).isTrue();
        assertThat(UserFieldType.ID.matches("abc---")).isTrue();
    }

    @DisplayName("비밀번호 형식 확인")
    @Test
    public void check_password() {
        assertThat(UserFieldType.PASSWORD.matches("a")).isFalse();
        assertThat(UserFieldType.PASSWORD.matches("12345678")).isFalse();
        assertThat(UserFieldType.PASSWORD.matches("########")).isFalse();

        assertThat(UserFieldType.PASSWORD.matches("Abcde123#")).isTrue();
        assertThat(UserFieldType.PASSWORD.matches("abcde1234#")).isFalse();
        assertThat(UserFieldType.PASSWORD.matches("ABCD1234")).isFalse();
        assertThat(UserFieldType.PASSWORD.matches("abcd##@%")).isFalse();
    }

    @DisplayName("이메일 형식 확인")
    @Test
    public void check_email() {
        assertThat(UserFieldType.EMAIL.matches("a")).isFalse();
        assertThat(UserFieldType.EMAIL.matches("a@b")).isFalse();
        assertThat(UserFieldType.EMAIL.matches("doo@.com")).isFalse();

        assertThat(UserFieldType.EMAIL.matches("doo@naver.com")).isTrue();
    }

    @DisplayName("휴대폰 번호 형식 확인")
    @Test
    public void check_cellphone_number() {
        assertThat(UserFieldType.CELLPHONE.matches("01123456789")).isFalse();
        assertThat(UserFieldType.CELLPHONE.matches("010123456789")).isFalse();

        assertThat(UserFieldType.CELLPHONE.matches("01012345678")).isTrue();
    }

    @DisplayName("닉네임 형식 확인")
    @Test
    public void check_nickname() {
        assertThat(UserFieldType.NICKNAME.matches("a")).isFalse();
        assertThat(UserFieldType.NICKNAME.matches("ㄱ")).isFalse();
        assertThat(UserFieldType.NICKNAME.matches("ㅏ")).isFalse();
        assertThat(UserFieldType.NICKNAME.matches("########")).isFalse();

        assertThat(UserFieldType.NICKNAME.matches("닉네임")).isTrue();
        assertThat(UserFieldType.NICKNAME.matches("correctNickname")).isTrue();
    }
}
