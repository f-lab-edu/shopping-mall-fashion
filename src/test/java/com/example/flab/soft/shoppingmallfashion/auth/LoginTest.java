package com.example.flab.soft.shoppingmallfashion.auth;

import com.example.flab.soft.shoppingmallfashion.auth.jwt.LoginRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class LoginTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @Value("${authorization.user.token}")
    String accessToken;

    @Value("${authorization.user.email}")
    String userEmail;

    @Value("${authorization.user.raw-password}")
    String userPassword;
    static final String WRONG_PASSWORD = "wrongPassword";

    @Test
    @DisplayName("로그인 정보가 잘못되면 401 응답")
    void whenLoginWithWrongPassword_thenReturn401() throws Exception {
        mvc.perform(
                        post("/login")
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
                        post("/login")
                                .content(mapper.writeValueAsString(LoginRequest.builder()
                                        .username(userEmail)
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
                                .header("Authorization", accessToken)
                )
                .andExpect(status().is(200));
    }
}
