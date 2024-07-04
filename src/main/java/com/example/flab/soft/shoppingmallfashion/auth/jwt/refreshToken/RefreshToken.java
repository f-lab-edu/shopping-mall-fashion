package com.example.flab.soft.shoppingmallfashion.auth.jwt.refreshToken;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
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
    private LocalDateTime expiration;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public RefreshToken(String token, LocalDateTime expiration, Long userId, LocalDateTime createdAt,
                        LocalDateTime updatedAt) {
        this.token = token;
        this.expiration = expiration;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
