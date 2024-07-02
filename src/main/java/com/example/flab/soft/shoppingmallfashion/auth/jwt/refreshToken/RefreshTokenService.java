package com.example.flab.soft.shoppingmallfashion.auth.jwt.refreshToken;

import com.example.flab.soft.shoppingmallfashion.auth.AuthUser;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.TokenProvider;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import java.time.Instant;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final Long EXPIRATION_TIME;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, TokenProvider tokenProvider,
                               @Value("${jwt.refresh-token-validation-time}") Long refreshExpirationTime) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenProvider = tokenProvider;
        this.EXPIRATION_TIME = refreshExpirationTime;
    }

    @Transactional(noRollbackFor = ApiException.class)
    public void validate(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_TOKEN));

        if (isExpired(refreshToken)) {
            refreshTokenRepository.delete(refreshToken);
            throw new ApiException(ErrorEnum.TOKEN_EXPIRED);
        }
    }

    private boolean isExpired(RefreshToken token) {
        return token.getExpiration().isBefore(Instant.now());
    }

    @Transactional
    public String getNewToken(AuthUser authUser) {
        Optional<RefreshToken> token = refreshTokenRepository.findByUserId(authUser.getId());
        token.ifPresent(refreshTokenRepository::delete);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(tokenProvider.createRefreshToken(authUser))
                .expiration(Instant.now().plusMillis(EXPIRATION_TIME))
                .userId(authUser.getId())
                .build();
        refreshTokenRepository.save(refreshToken);

        return refreshToken.getToken();
    }

    public void delete(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_TOKEN));
        refreshTokenRepository.delete(refreshToken);
    }
}
