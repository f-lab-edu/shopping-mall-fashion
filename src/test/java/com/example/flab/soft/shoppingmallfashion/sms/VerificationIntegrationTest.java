package com.example.flab.soft.shoppingmallfashion.sms;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.flab.soft.shoppingmallfashion.common.RedisUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class VerificationIntegrationTest {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    private static final String REDIS_VERIFY_CODE_KEY_PREFIX = "verification-code:";
    private static final String REDIS_VERIFIED_ID_PREFIX = "verified-id:";
    private static final String PHONE_NUMBER = "01012345678";
    private static final String EMAIL = "test@gmail.com";
    private static final String VERIFICATION_CODE = "1234";
    private static final String WRONG_VERIFICATION_CODE = "4321";

    @AfterEach
    void tearDown() {
        redisUtils.deleteData(REDIS_VERIFY_CODE_KEY_PREFIX + PHONE_NUMBER);
        redisUtils.deleteData(REDIS_VERIFIED_ID_PREFIX + PHONE_NUMBER);
    }

    @Test
    @DisplayName("인증코드가 일치하면 전화번호를 캐싱한다.")
    void whenCodeMatch_thenCacheNumber() throws Exception {
        //given
        redisUtils.setData(REDIS_VERIFY_CODE_KEY_PREFIX + PHONE_NUMBER, VERIFICATION_CODE, 5000L);
        //when
        mvc.perform(post("/api/v1/verification/verified-id")
                .content(mapper.writeValueAsString(VerifyCodeRequest.builder()
                        .code(VERIFICATION_CODE)
                        .verificationId(PHONE_NUMBER)
                        .build()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        //then
        assertThat(redisUtils.getData(REDIS_VERIFIED_ID_PREFIX + PHONE_NUMBER))
                .isNotNull();
    }

    @Test
    @DisplayName("인증코드가 일치하면 이메일을 캐싱한다.")
    void whenCodeMatch_thenCacheEmail() throws Exception {
        //given
        redisUtils.setData(REDIS_VERIFY_CODE_KEY_PREFIX + EMAIL, VERIFICATION_CODE, 5000L);
        //when
        mvc.perform(post("/api/v1/verification/verified-id")
                        .content(mapper.writeValueAsString(VerifyCodeRequest.builder()
                                .code(VERIFICATION_CODE)
                                .verificationId(EMAIL)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        //then
        assertThat(redisUtils.getData(REDIS_VERIFIED_ID_PREFIX + EMAIL))
                .isNotNull();
    }

    @Test
    @DisplayName("인증코드가 일치하지 않으면 400응답")
    void whenCodeDoesNotMatch_thenReturn400() throws Exception {
        //given
        redisUtils.setData(REDIS_VERIFY_CODE_KEY_PREFIX + PHONE_NUMBER, VERIFICATION_CODE, 5000L);
        //when
        mvc.perform(post("/api/v1/verification/verified-id")
                .content(mapper.writeValueAsString(VerifyCodeRequest.builder()
                        .code(WRONG_VERIFICATION_CODE)
                        .verificationId(PHONE_NUMBER)
                        .build()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }
}
