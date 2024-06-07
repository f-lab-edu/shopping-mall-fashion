package com.example.flab.soft.shoppingmallfashion.user.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.SecondaryTable;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SecondaryTable(name = "user_signin", pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_id"))
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
    @AttributeOverrides({
            @AttributeOverride(
                    name = "signinId",
                    column = @Column(table = "user_signin", name = "signin_id")),
            @AttributeOverride(
                    name = "password",
                    column = @Column(table = "user_signin", name = "password"))
    })

    @Embedded
    private UserSigninInfo userSigninInfo;
    private String name;
    private String email;
    private String cellphoneNumber;
    private String nickname;
    private LocalDate createdAt;

    @Builder
    public UserEntity(Long id, String signinId, String password, String name, String email, String cellphoneNumber,
                      String nickname, LocalDate createdAt) {
        this.id = id;
        this.userSigninInfo = new UserSigninInfo(signinId, password);
        this.name = name;
        this.email = email;
        this.cellphoneNumber = cellphoneNumber;
        this.nickname = nickname;
        this.createdAt = createdAt;
    }
}
