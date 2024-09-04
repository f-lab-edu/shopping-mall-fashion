package com.example.flab.soft.shoppingmallfashion.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.flab.soft.shoppingmallfashion.WithMockCustomUser;
import com.example.flab.soft.shoppingmallfashion.sms.VerificationService;
import com.example.flab.soft.shoppingmallfashion.user.controller.UserController;
import com.example.flab.soft.shoppingmallfashion.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@WithMockCustomUser
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private UserService userService;
    @MockBean
    private VerificationService verificationService;
    static final String CORRECT_FORM = "correct@gmail.com";
    static final String WRONG_FORM = "@@gmail.com";

    @DisplayName("필드 형식이 잘못된 경우 400 에러")
    @Test
    void whenSignUpWithBadField_thenReturn400() throws Exception {
        // bad username
        mvc.perform(
                        post("/api/v1/users/signup")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .content(mapper.writeValueAsString(Map.of(
                                        "email", CORRECT_FORM,
                                        "password", "Correct1#",
                                        "realName", "correct",
                                        "cellphoneNumber", "01092345678",
                                        "nickname", "correct"
                                )))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200));

        mvc.perform(
                        post("/api/v1/users/signup")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
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
}
