package com.example.flab.soft.shoppingmallfashion.user.domain;

import com.example.flab.soft.shoppingmallfashion.user.service.UserDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
    private String email;
    private String password;
    private String realName;
    private String cellphoneNumber;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public User(Long id, String email, String password, String realName, String cellphoneNumber, String nickname,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.realName = realName;
        this.cellphoneNumber = cellphoneNumber;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void changePassword(String password) {
        this.password = password;
        renewUpdatedAt();
    }

    public UserDto changeEmail(String email) {
        this.email = email;
        renewUpdatedAt();
        return toUserDto();
    }

    public UserDto changeRealName(String realName) {
        this.realName = realName;
        renewUpdatedAt();
        return toUserDto();
    }

    public UserDto changeCellphone(String cellphoneNumber) {
        this.cellphoneNumber = cellphoneNumber;
        renewUpdatedAt();
        return toUserDto();
    }

    public UserDto changeNickname(String nickname) {
        this.nickname = nickname;
        renewUpdatedAt();
        return toUserDto();
    }

    private void renewUpdatedAt() {
        updatedAt = LocalDateTime.now();
    }

    private UserDto toUserDto() {
        return UserDto.builder()
                .email(email)
                .realName(realName)
                .cellphoneNumber(cellphoneNumber)
                .nickname(nickname)
                .createdAt(createdAt)
                .build();
    }
}
