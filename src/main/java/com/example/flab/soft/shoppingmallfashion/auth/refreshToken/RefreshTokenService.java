package com.example.flab.soft.shoppingmallfashion.auth.refreshToken;

import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetails.AuthUser;
import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetailsService.CrewAuthService;
import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetailsService.UserAuthService;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.Role;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.dto.TokenBuildDto;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.TokenProvider;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.dto.NewTokensDto;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {
    private final UserAuthService userAuthService;
    private final CrewAuthService crewAuthService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final Long EXPIRATION_TIME_MILLIS;

    public RefreshTokenService(UserAuthService userAuthService, CrewAuthService crewAuthService,
                               RefreshTokenRepository refreshTokenRepository, TokenProvider tokenProvider,
                               @Value("${jwt.refresh-token-validation-time}") Long refreshExpirationTime) {
        this.userAuthService = userAuthService;
        this.crewAuthService = crewAuthService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenProvider = tokenProvider;
        this.EXPIRATION_TIME_MILLIS = refreshExpirationTime;
    }

    @Transactional
    public NewTokensDto renew(String token) {
        if (token == null || !tokenProvider.validateToken(token)) {
            throw new ApiException(ErrorEnum.INVALID_TOKEN);
        }

        String subject = tokenProvider.getSubject(token);

        Role role = Role.valueOf(parseRole(subject));
        String username = parseUsername(subject);

        AuthUser authUser = null;
        switch (role) {
            case USER -> authUser = (AuthUser) userAuthService.loadUserByUsername(username);
            case CREW -> authUser = (AuthUser) crewAuthService.loadUserByUsername(username);
        }

        RefreshToken oldToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_TOKEN));

        if (isExpired(oldToken)) {
            throw new ApiException(ErrorEnum.TOKEN_EXPIRED);
        }

        if (authUser != null) {
            TokenBuildDto tokenBuildDto = toTokenBuildDto(authUser);
            RefreshToken newToken = buildNewToken(tokenBuildDto);
            refreshTokenRepository.save(newToken);
            return buildTokensDto(tokenBuildDto, newToken);
        } else throw new ApiException(ErrorEnum.INVALID_TOKEN);
    }

    private boolean isExpired(RefreshToken token) {
        return token.getExpiration().isBefore(LocalDateTime.now());
    }

    @Transactional
    public NewTokensDto getNewToken(TokenBuildDto tokenBuildDto) {
        RefreshToken newToken = buildNewToken(tokenBuildDto);
        refreshTokenRepository.save(newToken);

        return buildTokensDto(tokenBuildDto, newToken);
    }

    private NewTokensDto buildTokensDto(TokenBuildDto tokenBuildDto, RefreshToken newToken) {
        return NewTokensDto.builder()
                .accessToken(tokenProvider.createAccessToken(tokenBuildDto))
                .refreshToken(newToken.getToken())
                .build();
    }

    private RefreshToken buildNewToken(TokenBuildDto tokenBuildDto) {
        return RefreshToken.builder()
                .token(tokenProvider.createRefreshToken(tokenBuildDto))
                .expiration(LocalDateTime.now().plus(EXPIRATION_TIME_MILLIS, ChronoUnit.MILLIS))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private TokenBuildDto toTokenBuildDto(AuthUser authUser) {
        return TokenBuildDto.builder()
                .subject(authUser.getRole().name() + " " + authUser.getEmail())
                .build();
    }

    private String parseRole(String subject) {
        return subject.split(" ")[0];
    }

    private String parseUsername(String subject) {
        return subject.split(" ")[1];
    }
}
