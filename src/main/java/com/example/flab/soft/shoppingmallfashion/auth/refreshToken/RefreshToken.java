package com.example.flab.soft.shoppingmallfashion.auth.refreshToken;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "refresh_tokens")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
    private String token;
    private LocalDateTime expiration;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public RefreshToken(String token, LocalDateTime expiration, LocalDateTime createdAt,
                        LocalDateTime updatedAt) {
        this.token = token;
        this.expiration = expiration;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
