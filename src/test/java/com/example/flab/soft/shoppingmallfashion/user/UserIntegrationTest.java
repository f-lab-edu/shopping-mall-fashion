package com.example.flab.soft.shoppingmallfashion.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
class UserIntegrationTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    static final String USER_EMAIL = "correct@gmail.com";
    static final String USER_PASSWORD = "Correct1#";
    static final String UPDATED_EMAIL = "user4@example.com";
    static final String UPDATED_NAME = "User Four";
    static final String UPDATED_CELLPHONE = "01033333333";
    static final String UPDATED_NICKNAME = "userfour";
    static final String UPDATED_PASSWORD = "Testuser4#";

    String accessToken;
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

        accessToken = "Bearer " + tokenResponse.getResponse().getAccessToken();
    }

    @DisplayName("내 정보 조회")
    @Test
    void my_info() throws Exception {


        mvc.perform(
                        get("/api/v1/users/me")
                                .header("Authorization", accessToken)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.response.email").value(USER_EMAIL));

    }

    @DisplayName("이메일 수정")
    @Test
    void update_email() throws Exception {
        mvc.perform(
                        patch("/api/v1/users/me?type=email")
                                .header("Authorization", accessToken)
                                .content(mapper.writeValueAsString(Map.of(
                                        "value", UPDATED_EMAIL
                                )))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.response.email").value(UPDATED_EMAIL));
    }

    @DisplayName("실명 수정")
    @Test
    void update_real_name() throws Exception {
        mvc.perform(
                        patch("/api/v1/users/me?type=realName")
                                .header("Authorization", accessToken)
                                .content(mapper.writeValueAsString(Map.of(
                                        "value", UPDATED_NAME
                                )))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.response.realName").value(UPDATED_NAME));
    }

    @DisplayName("휴대폰 번호 수정")
    @Test
    void update_cellphone_number() throws Exception {
        mvc.perform(
                        patch("/api/v1/users/me?type=cellphone")
                                .header("Authorization", accessToken)
                                .content(mapper.writeValueAsString(Map.of(
                                        "value", UPDATED_CELLPHONE
                                )))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.response.cellphoneNumber").value(UPDATED_CELLPHONE));
    }

    @DisplayName("닉네임 수정")
    @Test
    void update_nickname() throws Exception {
        mvc.perform(
                        patch("/api/v1/users/me?type=nickname")
                                .header("Authorization", accessToken)
                                .content(mapper.writeValueAsString(Map.of(
                                        "value", UPDATED_NICKNAME
                                )))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.response.nickname").value(UPDATED_NICKNAME));
    }

    @DisplayName("비밀번호 변경")
    @Test
    void change_password() throws Exception {
        mvc.perform(
                        patch("/api/v1/users/me/password")
                                .header("Authorization", accessToken)
                                .content(mapper.writeValueAsString(Map.of(
                                        "currentPassword", USER_PASSWORD,
                                        "newPassword", UPDATED_PASSWORD
                                )))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200));
    }

    @DisplayName("회원 탈퇴")
    @Test
    void withdraw_member() throws Exception {
        mvc.perform(
                        delete("/api/v1/users/me")
                                .header("Authorization", accessToken)
                )
                .andExpect(status().is(200));

        mvc.perform(
                        get("/api/v1/users/me")
                                .header("Authorization", accessToken)
                )
                .andExpect(status().is(401));
    }
}