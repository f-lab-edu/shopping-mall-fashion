package com.example.flab.soft.shoppingmallfashion.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
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
public class AuthenticationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @DisplayName("로그인 정보가 맞는 경우 200 응답")
    @Test
    void whenSignUpWithRightInfo_thenReturn200() throws Exception {
        mvc.perform(
                        post("/api/v1/users/signup")
                                .content(mapper.writeValueAsString(Map.of(
                                        "username", "valid",
                                        "password", "Correct1#",
                                        "realName", "correct",
                                        "email", "correct@gmail.com",
                                        "cellphoneNumber", "01012345678",
                                        "nickname", "correct"
                                )))
                                .contentType(MediaType.APPLICATION_JSON)
                );

        mvc.perform(
                        post("/api/v1/users/login")
                                .param("username", "valid")
                                .param("password", "Correct1#")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is(200));
    }

    @DisplayName("로그인 정보가 잘못된 경우 401 응답")
    @Test
    void whenSignUpWithWrongInfo_thenReturn401() throws Exception {
        mvc.perform(
                        post("/api/v1/users/login")
                                .param("username", "valid")
                                .param("password", "Correct1#")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is(401));
    }
}
