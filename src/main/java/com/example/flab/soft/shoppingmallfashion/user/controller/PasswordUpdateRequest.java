package com.example.flab.soft.shoppingmallfashion.user.controller;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordUpdateRequest {
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).*$",
            message = "허용되지 않는 비밀번호 형식입니다.")
    @Size(min = 8, max = 20, message = "비밀번호의 길이는 8자 이상 20자 이하입니다.")
    private String currentPassword;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).*$",
            message = "허용되지 않는 비밀번호 형식입니다.")
    @Size(min = 8, max = 20, message = "비밀번호의 길이는 8자 이상 20자 이하입니다.")
    private String newPassword;
    @Builder

    public PasswordUpdateRequest(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
}
