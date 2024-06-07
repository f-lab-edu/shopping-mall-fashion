package com.example.flab.soft.shoppingmallfashion.user.service;

import com.example.flab.soft.shoppingmallfashion.user.service.exception.badField.SignUpBadEmailException;
import com.example.flab.soft.shoppingmallfashion.user.service.exception.badField.SignUpBadIdException;
import com.example.flab.soft.shoppingmallfashion.user.service.exception.badField.SignUpBadNicknameException;
import com.example.flab.soft.shoppingmallfashion.user.service.exception.badField.SignUpBadPasswordException;
import com.example.flab.soft.shoppingmallfashion.user.service.exception.badField.SignUpBadPhoneNumberException;
import java.util.regex.Pattern;

public enum UserFieldType {
    ID("^(?!\\d+$)(?!.*([a-z0-9_-])\\1{4,})[a-z0-9_-]{5,20}$", new SignUpBadIdException()),
    PASSWORD("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$", new SignUpBadPasswordException()),
    EMAIL("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$", new SignUpBadEmailException()),
    CELLPHONE("^010[0-9]{8}$", new SignUpBadPhoneNumberException()),
    NICKNAME("^(?=.*[a-zA-Z0-9가-힣])[a-zA-Z0-9가-힣]{2,16}$", new SignUpBadNicknameException());

    private final String regex;
    private final RuntimeException exception;

    UserFieldType(String regex, RuntimeException exception) {
        this.regex = regex;
        this.exception = exception;
    }

    public void check(String string) {
        if (!matches(string)) throw exception;
    }

    public boolean matches(String string) {
        return string != null && Pattern.matches(regex, string);
    }
}
