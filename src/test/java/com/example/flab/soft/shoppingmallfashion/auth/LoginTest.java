package com.example.flab.soft.shoppingmallfashion.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.flab.soft.shoppingmallfashion.auth.jwt.LoginRequest;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.dto.TokenResponse;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class LoginTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    static final String USER_EMAIL = "correct@gmail.com";
    static final String USER_PASSWORD = "Correct1#";
    static final String WRONG_PASSWORD = "wrongPassword";
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
    }
    @Test
    @DisplayName("로그인 정보가 잘못되면 401 응답")
    void whenLoginWithWrongPassword_thenReturn401() throws Exception {
        mvc.perform(
                        post("/users/login")
                                .content(mapper.writeValueAsString(LoginRequest.builder()
                                        .username(USER_EMAIL)
                                        .password(WRONG_PASSWORD)
                                        .build()))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(401))
                .andExpect(header().doesNotExist("Authorization"));
    }

    @Test
    @DisplayName("로그인 성공시 토큰 발급")
    void whenLoginSuccess_thenReturnWithToken() throws Exception {
        mvc.perform(
                        post("/api/v1/users/signup")
                                .content(mapper.writeValueAsString(Map.of(
                                        "email", "logintestuser@test.com",
                                        "password", USER_PASSWORD,
                                        "realName", "loginTestUser1",
                                        "cellphoneNumber", "01087345678",
                                        "nickname", "LoginTestUser1"
                                )))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200));

        mvc.perform(
                        post("/users/login")
                                .content(mapper.writeValueAsString(LoginRequest.builder()
                                        .username("logintestuser@test.com")
                                        .password(USER_PASSWORD)
                                        .build()))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.response.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.response.refreshToken").isNotEmpty());
    }

    @Test
    @DisplayName("토큰이 있으면 401 에러가 발생하지 않는다")
    void whenHaveToken_thenReturn200() throws Exception {
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

        String accessToken = "Bearer " + tokenResponse.getResponse().getAccessToken();
        //without token
        mvc.perform(
                        get("/api/v1/users/me")
                )
                .andExpect(status().is(401));

        //with token
        mvc.perform(
                        get("/api/v1/users/me")
                                .header("Authorization", accessToken)
                )
                .andExpect(status().is(200));
    }
}
