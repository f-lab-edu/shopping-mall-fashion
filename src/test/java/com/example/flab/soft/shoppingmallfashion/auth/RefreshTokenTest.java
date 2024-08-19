package com.example.flab.soft.shoppingmallfashion.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.flab.soft.shoppingmallfashion.auth.jwt.LoginRequest;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.dto.TokenResponse;
import com.example.flab.soft.shoppingmallfashion.auth.refreshToken.TokenRefreshRequest;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class RefreshTokenTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    static final String USER_EMAIL = "correct@gmail.com";
    static final String USER_PASSWORD = "Correct1#";
    String refreshToken;
    @Value("${authorization.user.expired-token}")
    String expiredToken;

    @BeforeEach
    void setUp() throws Exception {
        mvc.perform(
                post("/api/v1/users/signup")
                        .content(mapper.writeValueAsString(Map.of(
                                "email", USER_EMAIL,
                                "password", USER_PASSWORD,
                                "realName", "correct",
                                "cellphoneNumber", "01092345678",
                                "nickname", "correct"
                        )))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        SuccessResult<TokenResponse> tokenResponse = mapper.readValue(
                mvc.perform(
                        post("/users/login")
                                .content(mapper.writeValueAsString(LoginRequest.builder()
                                        .username(USER_EMAIL)
                                        .password(USER_PASSWORD)
                                        .build()))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andReturn().getResponse().getContentAsString(),
                mapper.getTypeFactory().constructParametricType(SuccessResult.class, TokenResponse.class));

        refreshToken = tokenResponse.getResponse().getRefreshToken();
    }

    @Test
    @DisplayName("유효한 refresh 토큰 제공시 200응답과 함께 새로운 access 토큰과 refresh 토큰을 준다.")
    void whenHaveValidRefreshToken_thenReturn200() throws Exception {
        mvc.perform(
                        post("/api/v1/auth/refresh-token")
                                .content(mapper.writeValueAsString(TokenRefreshRequest.builder()
                                        .refreshToken(refreshToken)
                                        .build()))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.response.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.response.refreshToken").isNotEmpty());
    }

    @Test
    @DisplayName("유효하지 않은 refresh 토큰 제공시 400 응답")
    void whenInvalidToken_thenReturn400() throws Exception {
        mvc.perform(
                        post("/api/v1/auth/refresh-token")
                                .content(mapper.writeValueAsString(TokenRefreshRequest.builder()
                                        .refreshToken("wrongRefreshToken")
                                        .build()))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").value(ErrorEnum.INVALID_TOKEN.getCode()));
    }

    @Test
    @DisplayName("유효 기간이 지난 refresh 토큰 제공시 400 응답")
    void whenExpired_thenReturn400() throws Exception {
        mvc.perform(
                        post("/api/v1/auth/refresh-token")
                                .content(mapper.writeValueAsString(TokenRefreshRequest.builder()
                                        .refreshToken(expiredToken)
                                        .build()))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").value(ErrorEnum.TOKEN_EXPIRED.getCode()));
    }
}
