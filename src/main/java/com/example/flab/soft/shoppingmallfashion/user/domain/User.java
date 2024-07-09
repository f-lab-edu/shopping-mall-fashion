package com.example.flab.soft.shoppingmallfashion.user.domain;

import com.example.flab.soft.shoppingmallfashion.user.service.UserDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();
    @UpdateTimestamp
    private LocalDateTime updatedAt  = LocalDateTime.now();;
    private Boolean withdrawal = false;

    @Builder
    public User(String email, String password, String realName, String cellphoneNumber, String nickname) {
        this.email = email;
        this.password = password;
        this.realName = realName;
        this.cellphoneNumber = cellphoneNumber;
        this.nickname = nickname;
    }


    public void changePassword(String password) {
        this.password = password;
    }

    public UserDto update(String fieldName, String value) {
        if (Objects.equals(fieldName, "email")) {
            email = value;
        }
        if (Objects.equals(fieldName, "realName")) {
            realName = value;
        }
        if (Objects.equals(fieldName, "cellphone")) {
            cellphoneNumber = value;
        }
        if (Objects.equals(fieldName, "nickname")) {
            nickname = value;
        }
        return toUserDto();
    }

    public void withdraw() {
        withdrawal = true;
    }

    public boolean isInactivated(){
        return withdrawal;
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
