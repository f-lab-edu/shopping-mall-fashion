package com.example.flab.soft.shoppingmallfashion.auth;

import com.example.flab.soft.shoppingmallfashion.auth.jwt.LoginRequest;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.TokenResponse;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.*;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class LoginTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    static final String EMAIL = "testUser@gmail.com";
    static final String PASSWORD = "TestUser1#";
    static final String WRONG_PASSWORD = "wrongPassword";

    @BeforeEach
    void beforeEach() {
        userRepository.save(User.builder()
                .id(1L)
                .email(EMAIL)
                .password(passwordEncoder.encode(PASSWORD))
                .realName("testUser")
                .cellphoneNumber("01012345678")
                .nickname("testUser")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .withdrawal(false)
                .build());
    }

    @Test
    @DisplayName("로그인 정보가 잘못되면 401 응답")
    void whenLoginWithWrongPassword_thenReturn401() throws Exception {
        mvc.perform(
                        post("/login")
                                .content(mapper.writeValueAsString(LoginRequest.builder()
                                        .username(EMAIL)
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
        MvcResult mvcResult = mvc.perform(
                        post("/login")
                                .content(mapper.writeValueAsString(LoginRequest.builder()
                                        .username(EMAIL)
                                        .password(PASSWORD)
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
    @DisplayName("토큰이 있으면 401 에러가 발생하지 않는다")
    void whenHaveToken_thenReturn200() throws Exception {
        MvcResult mvcResult = mvc.perform(
                        post("/login")
                                .content(mapper.writeValueAsString(LoginRequest.builder()
                                        .username(EMAIL)
                                        .password(PASSWORD)
                                        .build()))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        SuccessResult<TokenResponse> result = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                mapper.getTypeFactory().constructParametricType(SuccessResult.class, TokenResponse.class));
        String accessToken = result.getResponse().getAccessToken();

        //without token
        mvc.perform(
                        get("/api/v1/users/me")
                )
                .andExpect(status().is(401));

        //with token
        mvc.perform(
                        get("/api/v1/users/me")
                                .header("Authorization", "Bearer " + accessToken)
                )
                .andExpect(status().is(200));
    }
}
