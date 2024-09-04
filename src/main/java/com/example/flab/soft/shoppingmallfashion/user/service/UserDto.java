package com.example.flab.soft.shoppingmallfashion.user.service;

import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDto {
    private String email;
    private String realName;
    private String cellphoneNumber;
    private String nickname;
    private Boolean isEmailVerified;
    private Boolean isPhoneNumberVerified;
    private LocalDateTime createdAt;

    @Builder
    public UserDto(String email, String realName, String cellphoneNumber,
                   String nickname, Boolean isEmailVerified,
                   Boolean isPhoneNumberVerified, LocalDateTime createdAt) {
        this.email = email;
        this.realName = realName;
        this.cellphoneNumber = cellphoneNumber;
        this.nickname = nickname;
        this.isEmailVerified = isEmailVerified;
        this.isPhoneNumberVerified = isPhoneNumberVerified;
        this.createdAt = createdAt;
    }

    public static UserDto of(User user) {
        return builder()
                .email(user.getEmail())
                .realName(user.getRealName())
                .cellphoneNumber(user.getCellphoneNumber())
                .nickname(user.getNickname())
                .isEmailVerified(user.getIsEmailVerified())
                .isPhoneNumberVerified(user.getIsPhoneNumberVerified())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
