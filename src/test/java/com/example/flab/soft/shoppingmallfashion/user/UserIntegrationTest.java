package com.example.flab.soft.shoppingmallfashion.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class UserIntegrationTest {
    static final String CORRECT_FORM = "correct@gmail.com";
    static final String WRONG_FORM = "@@gmail.com";
    static final String DUPLICATED_FIELD = "correct@gmail.com";
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Value("${authorization.user.token}")
    String accessToken;

    @Value("${authorization.user.email}")
    String userEmail;

    static final String UPDATED_EMAIL = "user4@example.com";
    static final String UPDATED_NAME = "User Four";
    static final String UPDATED_CELLPHONE = "01033333333";
    static final String UPDATED_NICKNAME = "userfour";

    @DisplayName("필드 형식이 잘못된 경우 400 에러")
    @Test
    void whenSignUpWithBadField_thenReturn400() throws Exception {
        // bad username
        mvc.perform(
                        post("/api/v1/users/signup")
                                .content(mapper.writeValueAsString(Map.of(
                                        "email", CORRECT_FORM,
                                        "password", "Correct1#",
                                        "realName", "correct",
                                        "cellphoneNumber", "01012345678",
                                        "nickname", "correct"
                                )))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200));

        mvc.perform(
                        post("/api/v1/users/signup")
                                .content(mapper.writeValueAsString(Map.of(
                                        "email", WRONG_FORM,
                                        "password", "Correct1#",
                                        "realName", "correct",
                                        "cellphoneNumber", "01012345678",
                                        "nickname", "correct"
                                )))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(400));
    }

    @DisplayName("필드 충돌시 409 에러")
    @Test
    void whenSignUpWithExistingValue_thenReturn409() throws Exception {
        mvc.perform(
                        post("/api/v1/users/signup")
                                .content(mapper.writeValueAsString(Map.of(
                                        "email", "correct@gmail.com",
                                        "password", "Correct1#",
                                        "realName", "correct",
                                        "cellphoneNumber", "01012345678",
                                        "nickname", "correct"
                                )))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200));

        mvc.perform(
                        post("/api/v1/users/signup")
                                .content(mapper.writeValueAsString(Map.of(
                                        "email", DUPLICATED_FIELD,
                                        "password", "Correct1#",
                                        "realName", "correct",
                                        "cellphoneNumber", "01012345678",
                                        "nickname", "correct"
                                )))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(409));
    }

    @DisplayName("내 정보 조회")
    @Test
    void my_info() throws Exception {
        mvc.perform(
                        get("/api/v1/users/me")
                                .header("Authorization", accessToken)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.response.email").value(userEmail));

    }

    @DisplayName("이메일 수정")
    @Test
    void update_email() throws Exception {
        mvc.perform(
                        patch("/api/v1/users/me/email")
                                .header("Authorization", accessToken)
                                .content(mapper.writeValueAsString(Map.of(
                                        "email", UPDATED_EMAIL
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
                        patch("/api/v1/users/me/realName")
                                .header("Authorization", accessToken)
                                .content(mapper.writeValueAsString(Map.of(
                                        "realName", UPDATED_NAME
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
                        patch("/api/v1/users/me/cellphone")
                                .header("Authorization", accessToken)
                                .content(mapper.writeValueAsString(Map.of(
                                        "cellphoneNumber", UPDATED_CELLPHONE
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
                        patch("/api/v1/users/me/nickname")
                                .header("Authorization", accessToken)
                                .content(mapper.writeValueAsString(Map.of(
                                        "nickname", UPDATED_NICKNAME
                                )))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.response.nickname").value(UPDATED_NICKNAME));
    }
}