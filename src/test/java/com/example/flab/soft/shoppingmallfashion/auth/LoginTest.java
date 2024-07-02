package com.example.flab.soft.shoppingmallfashion.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class LoginTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("로그인 정보가 잘못되면 401 응답")
    void whenLoginWithWrongPassword_thenReturn401() throws Exception {
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
                        post("/login")
                                .content(mapper.writeValueAsString(Map.of(
                                        "username", "correct@gmail.com",
                                        "password", "wrongPassword"
                                )))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(401))
                .andExpect(header().doesNotExist("Authorization"));
    }

    @Test
    @DisplayName("로그인 성공시 토큰 발금")
    void whenLoginSuccess_thenReturnWithToken() throws Exception {
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
                        post("/login")
                                .content(mapper.writeValueAsString(Map.of(
                                        "username", "correct@gmail.com",
                                        "password", "Correct1#"
                                )))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andExpect(header().exists("Authorization"));
    }

    @Test
    @DisplayName("토큰이 있으면 401 에러가 발생하지 않는다")
    void whenHaveToken_thenReturn200() throws Exception {
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

        MvcResult mvcResult = mvc.perform(
                        post("/login")
                                .content(mapper.writeValueAsString(Map.of(
                                        "username", "correct@gmail.com",
                                        "password", "Correct1#"
                                )))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andExpect(header().exists("Authorization"))
                .andReturn();

        String token = mvcResult.getResponse().getHeader("Authorization");

        //without token
        mvc.perform(
                        get("/")
                )
                .andExpect(status().is(401));

        //with token
        mvc.perform(
                        get("/")
                                .header("Authorization", token)
                )
                .andExpect(status().is(200));
    }
}
