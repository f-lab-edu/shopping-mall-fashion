package com.example.flab.soft.shoppingmallfashion.auth.jwt.refreshToken;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
    private String token;
    private Instant expiration;
    private Long userId;

    @Builder
    public RefreshToken(String token, Instant expiration, Long userId) {
        this.token = token;
        this.expiration = expiration;
        this.userId = userId;
    }
}
