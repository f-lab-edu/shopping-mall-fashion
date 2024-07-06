package com.example.flab.soft.shoppingmallfashion.auth;

import com.example.flab.soft.shoppingmallfashion.auth.jwt.TokenProvider;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.TokenResponse;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.dto.TokenBuildDto;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.refreshToken.RefreshToken;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.refreshToken.RefreshTokenRepository;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.refreshToken.TokenRefreshRequest;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorResult;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.*;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class RefreshTokenTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenProvider tokenProvider;

    static final int TOKEN_EXPIRATION = 6000;
    static final TokenBuildDto TOKEN_BUILD_DTO = TokenBuildDto.builder()
            .subject("testUser@gmail.com")
            .claim("id", 1L)
            .build();
    static final TokenBuildDto INVALID_TOKEN_BUILD_DTO = TokenBuildDto.builder()
            .subject("invalid@gmail.com")
            .claim("id", 2L)
            .build();
    private RefreshToken refreshToken;
    @BeforeEach
    void beforeEach() {
        userRepository.save(User.builder()
                .id(1L)
                .email("testUser@gmail.com")
                .password("TestUser1#")
                .realName("testUser")
                .cellphoneNumber("01012345678")
                .nickname("testUser")
                .createdAt(LocalDate.now())
                .build());
    }

    @Test
    @DisplayName("유효한 refresh 토큰 제공시 200응답과 함께 새로운 access 토큰과 refresh 토큰을 준다.")
    void whenHaveValidRefreshToken_thenReturn200() throws Exception {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(tokenProvider.createRefreshToken(TOKEN_BUILD_DTO))
                .expiration(LocalDateTime.now().plusSeconds(TOKEN_EXPIRATION))
                .userId(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        refreshTokenRepository.save(refreshToken);

        MvcResult mvcResult = mvc.perform(
                        post("/api/v1/auth/refresh-token")
                                .content(mapper.writeValueAsString(TokenRefreshRequest.builder()
                                        .refreshToken(refreshToken.getToken())
                                        .build()))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andReturn();

        SuccessResult<TokenResponse> result = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                mapper.getTypeFactory().constructParametricType(SuccessResult.class, TokenResponse.class));
        TokenResponse tokenResponse = result.getResponse();

        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.getAccessToken()).isNotNull();
        assertThat(tokenResponse.getRefreshToken()).isNotNull();
    }

    @Test
    @DisplayName("유효하지 않은 refresh 토큰 제공시 400 응답")
    void whenInvalidToken_thenReturn400() throws Exception {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(tokenProvider.createRefreshToken(TOKEN_BUILD_DTO))
                .expiration(LocalDateTime.now().plusSeconds(TOKEN_EXPIRATION))
                .userId(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        refreshTokenRepository.save(refreshToken);

        MvcResult mvcResult = mvc.perform(
                        post("/api/v1/auth/refresh-token")
                                .content(mapper.writeValueAsString(TokenRefreshRequest.builder()
                                        .refreshToken(tokenProvider.createRefreshToken(INVALID_TOKEN_BUILD_DTO))
                                        .build()))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(400))
                .andReturn();

        ErrorResult result = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResult.class);
        assertThat(result.getCode()).isEqualTo(ErrorEnum.INVALID_TOKEN.getCode());
    }

    @Test
    @DisplayName("유효 기간이 지난 refresh 토큰 제공시 400 응답")
    void whenExpired_thenReturn400() throws Exception {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(tokenProvider.createRefreshToken(TOKEN_BUILD_DTO))
                .expiration(LocalDateTime.now())
                .userId(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        refreshTokenRepository.save(refreshToken);

        MvcResult mvcResult = mvc.perform(
                        post("/api/v1/auth/refresh-token")
                                .content(mapper.writeValueAsString(TokenRefreshRequest.builder()
                                        .refreshToken(refreshToken.getToken())
                                        .build()))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(ErrorEnum.TOKEN_EXPIRED.getStatus().value()))
                .andReturn();

        ErrorResult result = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResult.class);
        assertThat(result.getCode()).isEqualTo(ErrorEnum.TOKEN_EXPIRED.getCode());
    }
}
