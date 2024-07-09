package com.example.flab.soft.shoppingmallfashion.auth;

import com.example.flab.soft.shoppingmallfashion.auth.jwt.refreshToken.TokenRefreshRequest;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class RefreshTokenTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @Value("${authorization.user.refresh-token}")
    String refreshToken;
    @Value("${authorization.user.expired-token}")
    String expiredToken;

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
