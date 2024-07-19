package com.example.flab.soft.shoppingmallfashion.auth;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetails.AuthCrew;
import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetails.AuthUser;
import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetailsService.CrewAuthService;
import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetailsService.UserAuthService;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.TokenProvider;
import com.example.flab.soft.shoppingmallfashion.auth.refreshToken.RefreshToken;
import com.example.flab.soft.shoppingmallfashion.auth.refreshToken.RefreshTokenRepository;
import com.example.flab.soft.shoppingmallfashion.auth.refreshToken.RefreshTokenService;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {
    @Mock
    private UserAuthService userAuthService;
    @Mock
    private CrewAuthService crewAuthService;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private TokenProvider tokenProvider;
    @InjectMocks
    private RefreshTokenService refreshTokenService;
    public static final String USER = "USER";
    public static final String CREW = "CREW";

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
        String token = "token";
        String subject = USER + " user1@test.com";
        String username = "user1@test.com";
        LocalDateTime expiration = LocalDateTime.now().minusDays(1);

        when(tokenProvider.validateToken(token)).thenReturn(true);
        when(tokenProvider.getSubject(token)).thenReturn(subject);
        when(userAuthService.loadUserByUsername(username)).thenReturn(mock(AuthUser.class));
//        when(crewAuthService.loadUserByUsername(username)).thenReturn(mock(AuthCrew.class));
        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(
                RefreshToken.builder().token(token).expiration(expiration).build()));

        assertThatThrownBy(() -> refreshTokenService.renew(token))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("error", ErrorEnum.TOKEN_EXPIRED);
    }

    @Test
    @DisplayName("유저 토큰은 유저 인증 서비스를 이용한다")
    void whenRenewWithUserToken_thenUseUserAuthService() {
        String token = "token";
        String subject = USER + " user1@test.com";
        String username = "user1@test.com";
        LocalDateTime expiration = LocalDateTime.now().plusHours(1);

        when(tokenProvider.validateToken(token)).thenReturn(true);
        when(tokenProvider.getSubject(token)).thenReturn(subject);
        when(userAuthService.loadUserByUsername(username)).thenReturn(AuthUser.builder()
                .email(username)
                .build());
        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(
                RefreshToken.builder().token(token).expiration(expiration).build()));

        refreshTokenService.renew(token);

        verify(userAuthService).loadUserByUsername(username);
    }

    @Test
    @DisplayName("직원 토큰은 직원 인증 서비스를 이용한다")
    void whenRenewWithCrewToken_thenUseCrewAuthService() {
        String token = "token";
        String subject = CREW + " crew1@test.com";
        String username = "crew1@test.com";
        LocalDateTime expiration = LocalDateTime.now().plusHours(1);

        when(tokenProvider.validateToken(token)).thenReturn(true);
        when(tokenProvider.getSubject(token)).thenReturn(subject);
        when(crewAuthService.loadUserByUsername(username)).thenReturn(AuthCrew.builder()
                .email(username)
                .build());
        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(
                RefreshToken.builder().token(token).expiration(expiration).build()));

        refreshTokenService.renew(token);

        verify(crewAuthService).loadUserByUsername(username);
    }
}
