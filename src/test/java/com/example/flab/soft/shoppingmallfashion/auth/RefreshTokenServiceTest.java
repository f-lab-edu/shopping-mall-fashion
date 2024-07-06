package com.example.flab.soft.shoppingmallfashion.auth;

import com.example.flab.soft.shoppingmallfashion.auth.jwt.TokenProvider;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.dto.TokenBuildDto;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.dto.TokensDto;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.refreshToken.RefreshToken;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.refreshToken.RefreshTokenRepository;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.refreshToken.RefreshTokenService;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {
    @Mock
    private AuthService authService;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private TokenProvider tokenProvider;
    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(refreshTokenService, "EXPIRATION_TIME_MILLIS", 86400000L);
    }

    @Test
    @DisplayName("유효하지 않은 토큰은 INVALID_TOKEN 예외를 던진다.")
    void whenRenewWithInvalidToken_thenThrowApiException() {
        assertThatThrownBy(() -> refreshTokenService.renew("invalid-token"))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("error", ErrorEnum.INVALID_TOKEN);
    }

    @Test
    @DisplayName("유효기간이 지난 토큰은 TOKEN_EXPIRED 예외를 던진다.")
    void whenRenewWithExpiredToken_thenThrowApiException() {
        String token = "valid-token";
        String username = "testUser";
        LocalDateTime expiration = LocalDateTime.now().minusDays(1);

        when(tokenProvider.validateToken(token)).thenReturn(true);
        when(tokenProvider.getSubjectFromToken(token)).thenReturn(username);
        AuthUser mockAuthUser = mock(AuthUser.class);
        when(authService.loadUserByUsername(username)).thenReturn(mockAuthUser);
        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(
                RefreshToken.builder()
                        .token(token)
                        .expiration(expiration)
                        .build()));

        assertThatThrownBy(() -> refreshTokenService.renew(token))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("error", ErrorEnum.TOKEN_EXPIRED);
    }

    @Test
    @DisplayName("유효한 토큰이 들어오면 새로운 토큰을 발급한다.")
    void whenRenewWithValidToken_thenReturnNewTokens() {
        String token = "valid-token";
        String username = "testUser";
        AuthUser authUser = mock(AuthUser.class);
        RefreshToken oldToken = mock(RefreshToken.class);
        TokenBuildDto tokenBuildDto = TokenBuildDto.builder()
                .subject(username)
                .claim("id", 1L)
                .build();

        when(tokenProvider.validateToken(token)).thenReturn(true);
        when(tokenProvider.getSubjectFromToken(token)).thenReturn(username);
        when(authService.loadUserByUsername(username)).thenReturn(authUser);
        when(authUser.getId()).thenReturn(1L);
        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(oldToken));
        when(oldToken.getExpiration()).thenReturn(LocalDateTime.now().plusDays(1));
        when(tokenProvider.createAccessToken(any(TokenBuildDto.class))).thenReturn("new-access-token");
        when(tokenProvider.createRefreshToken(any(TokenBuildDto.class))).thenReturn("new-refresh-token");

        TokensDto tokensDto = refreshTokenService.renew(token);

        assertThat(tokensDto).isNotNull();
        assertThat(tokensDto.getAccessToken()).isEqualTo("new-access-token");
        assertThat(tokensDto.getRefreshToken()).isEqualTo("new-refresh-token");

        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }
}
