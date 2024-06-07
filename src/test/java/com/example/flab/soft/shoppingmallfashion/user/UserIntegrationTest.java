package com.example.flab.soft.shoppingmallfashion.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @DisplayName("필드 형식이 잘못된 경우 400 에러")
    @Test
    void whenSignUpWithBadField_thenReturn400() throws Exception {
        // bad username
        mvc.perform(
                        post("/users/signup")
                                .content(mapper.writeValueAsString(Map.of(
                                        "signinId", "bad",
                                        "password", "Correct1#",
                                        "name", "correct",
                                        "email", "correct@gmail.com",
                                        "cellphoneNumber", "01012345678",
                                        "nickname", "correct"
                                )))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(400));

        // bad password
        mvc.perform(
                        post("/users/signup")
                                .content(mapper.writeValueAsString(Map.of(
                                        "signinId", "correct",
                                        "password", "bad",
                                        "name", "correct",
                                        "email", "correct@gmail.com",
                                        "cellphoneNumber", "01012345678",
                                        "nickname", "correct"
                                )))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(400));

        // bad email
        mvc.perform(
                        post("/users/signup")
                                .content(mapper.writeValueAsString(Map.of(
                                        "signinId", "correct",
                                        "password", "Correct1#",
                                        "name", "correct",
                                        "email", "bad",
                                        "cellphoneNumber", "01012345678",
                                        "nickname", "correct"
                                )))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(400));

        // bad cellphone number
        mvc.perform(
                        post("/users/signup")
                                .content(mapper.writeValueAsString(Map.of(
                                        "signinId", "correct",
                                        "password", "Correct1#",
                                        "name", "correct",
                                        "email", "correct@gmail.com",
                                        "cellphoneNumber", "bad",
                                        "nickname", "correct"
                                )))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(400));

        // bad nickname
        mvc.perform(
                        post("/users/signup")
                                .content(mapper.writeValueAsString(Map.of(
                                        "signinId", "correct",
                                        "password", "Correct1#",
                                        "name", "correct",
                                        "email", "correct@gmail.com",
                                        "cellphoneNumber", "01012345678",
                                        "nickname", "b"
                                )))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(400));

        mvc.perform(
                        post("/users/signup")
                                .content(mapper.writeValueAsString(Map.of(
                                        "signinId", "correct",
                                        "password", "Correct1#",
                                        "name", "correct",
                                        "email", "correct@gmail.com",
                                        "cellphoneNumber", "01012345678",
                                        "nickname", "correct"
                                )))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200));
    }

    @DisplayName("필드 충돌시 409 에러")
    @Test
    void whenSignUpWithExistingValue_thenReturn409() throws Exception {
        mvc.perform(
                        post("/users/signup")
                                .content(mapper.writeValueAsString(Map.of(
                                        "signinId", "correct",
                                        "password", "Correct1#",
                                        "name", "correct",
                                        "email", "correct@gmail.com",
                                        "cellphoneNumber", "01012345678",
                                        "nickname", "correct"
                                )))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200));

        mvc.perform(
                        post("/users/signup")
                                .content(mapper.writeValueAsString(Map.of(
                                        "signinId", "correct",
                                        "password", "Correct1#",
                                        "name", "correct",
                                        "email", "correct@gmail.com",
                                        "cellphoneNumber", "01012345678",
                                        "nickname", "correct"
                                )))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(409));
    }
}