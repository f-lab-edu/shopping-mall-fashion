package com.example.flab.soft.shoppingmallfashion.user.domain;

import static com.example.flab.soft.shoppingmallfashion.util.NotNullValidator.*;

import com.example.flab.soft.shoppingmallfashion.common.BaseEntity;
import com.example.flab.soft.shoppingmallfashion.user.service.UserDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String realName;
    @Column(nullable = false)
    private String cellphoneNumber;
    @Column(nullable = false)
    private String nickname;
    private Boolean withdrawal = false;

    @Builder
    public User(Long id, String email, String password,
                String realName, String cellphoneNumber, String nickname) {
        this.id = id;
        this.email = requireNotNull(email);
        this.password = requireNotNull(password);
        this.realName = requireNotNull(realName);
        this.cellphoneNumber = requireNotNull(cellphoneNumber);
        this.nickname = requireNotNull(nickname);
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
                .createdAt(super.getCreatedAt())
                .build();
    }
}
