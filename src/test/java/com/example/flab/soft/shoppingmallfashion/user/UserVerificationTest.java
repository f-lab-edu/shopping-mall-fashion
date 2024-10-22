package com.example.flab.soft.shoppingmallfashion.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.flab.soft.shoppingmallfashion.auth.jwt.LoginRequest;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.dto.TokenResponse;
import com.example.flab.soft.shoppingmallfashion.common.RedisRepository;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserVerificationTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private RedisRepository redisRepository;
    static final String USER_VERIFIED_EMAIL = "correct@gmail.com";
    static final String USER_PASSWORD = "Correct1#";
    private static final String VERIFIED_PHONE_NUMBER = "01064763333";
    private static final String REDIS_VERIFIED_PHONE_NUMBER_PREFIX = "verified-phone-number:";
    private static final String REDIS_VERIFIED_EMAIL_PREFIX = "verified-email:";

    String accessToken;
    @BeforeEach
    void setUp() throws Exception {
        mvc.perform(
                post("/api/v1/users/signup")
                        .content(mapper.writeValueAsString(Map.of(
                                "email", USER_VERIFIED_EMAIL,
                                "password", USER_PASSWORD,
                                "realName", "correct",
                                "cellphoneNumber", VERIFIED_PHONE_NUMBER,
                                "nickname", "correct"
                        )))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        SuccessResult<TokenResponse> tokenResponse = mapper.readValue(
                mvc.perform(
                        post("/users/login")
                                .content(mapper.writeValueAsString(LoginRequest.builder()
                                        .username(USER_VERIFIED_EMAIL)
                                        .password(USER_PASSWORD)
                                        .build()))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andReturn().getResponse().getContentAsString(),
                mapper.getTypeFactory().constructParametricType(SuccessResult.class, TokenResponse.class));

        accessToken = "Bearer " + tokenResponse.getResponse().getAccessToken();
    }

    @AfterEach
    void tearDown() {
        redisRepository.deleteData(REDIS_VERIFIED_PHONE_NUMBER_PREFIX + VERIFIED_PHONE_NUMBER);
        redisRepository.deleteData(REDIS_VERIFIED_EMAIL_PREFIX + USER_VERIFIED_EMAIL);
    }

    @DisplayName("이메일 인증")
    @Test
    void verify_email() throws Exception {
        redisRepository.setData(REDIS_VERIFIED_EMAIL_PREFIX + USER_VERIFIED_EMAIL, "1", 5000L);
        Object data = redisRepository.getData(REDIS_VERIFIED_EMAIL_PREFIX + USER_VERIFIED_EMAIL);
        System.out.println("data = " + data);
        mvc.perform(
                        patch("/api/v1/users/me/verification/email")
                                .header("Authorization", accessToken)
                                .content(mapper.writeValueAsString(Map.of(
                                        "email", USER_VERIFIED_EMAIL)))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.response.isEmailVerified").value(true));
    }

    @DisplayName("전화번호 인증")
    @Test
    void verify_phone_number() throws Exception {
        redisRepository.setData(REDIS_VERIFIED_PHONE_NUMBER_PREFIX + VERIFIED_PHONE_NUMBER, "1", 5000L);
        mvc.perform(
                        patch("/api/v1/users/me/verification/phone-number")
                                .header("Authorization", accessToken)
                                .content(mapper.writeValueAsString(Map.of(
                                        "phoneNumber", VERIFIED_PHONE_NUMBER)))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.response.isPhoneNumberVerified").value(true));
    }

    @DisplayName("인증되지 않은 이메일 혹은 전화번호시 400 응답")
    @Test
    void whenIdNotVerified_thenReturn400() throws Exception {
        mvc.perform(
                        patch("/api/v1/users/me/verification/email")
                                .header("Authorization", accessToken)
                                .content(mapper.writeValueAsString(Map.of(
                                        "email", USER_VERIFIED_EMAIL)))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(400));
    }
}
