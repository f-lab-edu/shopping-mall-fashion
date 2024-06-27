package com.example.flab.soft.shoppingmallfashion.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
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
    private LocalDate createdAt;

    @Builder
    public User(Long id, String email, String password, String realName, String cellphoneNumber, String nickname,
                LocalDate createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.realName = realName;
        this.cellphoneNumber = cellphoneNumber;
        this.nickname = nickname;
        this.createdAt = createdAt;
    }
}
