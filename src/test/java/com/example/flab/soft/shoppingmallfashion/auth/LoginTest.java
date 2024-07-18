package com.example.flab.soft.shoppingmallfashion.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.flab.soft.shoppingmallfashion.auth.jwt.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
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
public class LoginTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @Value("${authorization.user.token}")
    String userAccessToken;
    @Value("${authorization.store-manager.token}")
    String crewAccessToken;

    @Value("${authorization.user.email}")
    String userEmail;
    @Value("${authorization.store-manager.email}")
    String crewEmail;

    @Value("${authorization.user.raw-password}")
    String userPassword;
    static final String WRONG_PASSWORD = "wrongPassword";

    @Test
    @DisplayName("로그인 정보가 잘못되면 401 응답")
    void whenLoginWithWrongPassword_thenReturn401() throws Exception {
        mvc.perform(
                        post("/users/login")
                                .content(mapper.writeValueAsString(LoginRequest.builder()
                                        .username(userEmail)
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
                                        "password", userPassword,
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
                                        .password(userPassword)
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
        //without token
        mvc.perform(
                        get("/api/v1/users/me")
                )
                .andExpect(status().is(401));

        //with token
        mvc.perform(
                        get("/api/v1/users/me")
                                .header("Authorization", userAccessToken)
                )
                .andExpect(status().is(200));
    }
}
