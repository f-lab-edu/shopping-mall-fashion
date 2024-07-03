package com.example.flab.soft.shoppingmallfashion.auth.jwt.refreshToken;

import com.example.flab.soft.shoppingmallfashion.auth.AuthService;
import com.example.flab.soft.shoppingmallfashion.auth.AuthUser;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.dto.TokenBuildDto;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.TokenProvider;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.dto.TokensDto;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import java.time.Instant;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {
    private final AuthService authService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final Long EXPIRATION_TIME;

    public RefreshTokenService(AuthService authService, RefreshTokenRepository refreshTokenRepository, TokenProvider tokenProvider,
                               @Value("${jwt.refresh-token-validation-time}") Long refreshExpirationTime) {
        this.authService = authService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenProvider = tokenProvider;
        this.EXPIRATION_TIME = refreshExpirationTime;
    }

    @Transactional(noRollbackFor = ApiException.class)
    public TokensDto renew(String token) {
        if (token == null || !tokenProvider.validateToken(token)) {
            throw new ApiException(ErrorEnum.INVALID_TOKEN);
        }

        String username = tokenProvider.getSubjectFromToken(token);
        AuthUser authUser = (AuthUser) authService.loadUserByUsername(username);

        RefreshToken oldToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_TOKEN));

        if (isExpired(oldToken)) {
            refreshTokenRepository.delete(oldToken);
            throw new ApiException(ErrorEnum.TOKEN_EXPIRED);
        }

        TokenBuildDto tokenBuildDto = extractTokenBuildData(authUser);
        RefreshToken newToken = buildNewToken(tokenBuildDto, authUser.getId());
        refreshTokenRepository.save(newToken);

        return buildTokensDto(tokenBuildDto, newToken);
    }

    private boolean isExpired(RefreshToken token) {
        return token.getExpiration().isBefore(Instant.now());
    }

    @Transactional
    public TokensDto getNewToken(TokenBuildDto tokenBuildDto) {
        Long userId = Long.valueOf(tokenBuildDto.getClaim("id"));
        Optional<RefreshToken> token = refreshTokenRepository.findByUserId(userId);
        token.ifPresent(refreshTokenRepository::delete);

        RefreshToken newToken = buildNewToken(tokenBuildDto, userId);
        refreshTokenRepository.save(newToken);

        return buildTokensDto(tokenBuildDto, newToken);
    }

    private TokensDto buildTokensDto(TokenBuildDto tokenBuildDto, RefreshToken newToken) {
        return TokensDto.builder()
                .accessToken(tokenProvider.createAccessToken(tokenBuildDto))
                .refreshToken(newToken.getToken())
                .build();
    }

    private RefreshToken buildNewToken(TokenBuildDto tokenBuildDto, Long userId) {
        return RefreshToken.builder()
                .token(tokenProvider.createRefreshToken(tokenBuildDto))
                .expiration(Instant.now().plusMillis(EXPIRATION_TIME))
                .userId(userId)
                .build();
    }

    private TokenBuildDto extractTokenBuildData(AuthUser authUser) {
        return TokenBuildDto.builder()
                .subject(authUser.getEmail())
                .claim("id", authUser.getId())
                .build();
    }
}
