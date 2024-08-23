package com.example.flab.soft.shoppingmallfashion.store.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
public class CrewSignUpRequest {
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$", message = "허용되지 않는 이메일 형식입니다.")
    private String email;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).*$",
            message = "허용되지 않는 비밀번호 형식입니다.")
    @Size(min = 8, max = 20, message = "비밀번호의 길이는 8자 이상 20자 이하입니다.")
    private String password;
    @NotBlank(message = "이름은 공백일 수 없습니다.")
    private String name;
    @Pattern(regexp = "^010[0-9]{8}$", message = "허용되지 않는 전화번호 형식입니다.")
    private String cellphoneNumber;
}
