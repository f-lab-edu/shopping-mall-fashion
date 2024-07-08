package com.example.flab.soft.shoppingmallfashion.user.service;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDto {
    private String email;
    private String realName;
    private String cellphoneNumber;
    private String nickname;
    private LocalDate createdAt;

    @Builder
    public UserDto(String email, String realName, String cellphoneNumber, String nickname, LocalDate createdAt) {
        this.email = email;
        this.realName = realName;
        this.cellphoneNumber = cellphoneNumber;
        this.nickname = nickname;
        this.createdAt = createdAt;
    }
}
