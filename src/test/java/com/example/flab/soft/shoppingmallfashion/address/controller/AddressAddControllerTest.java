package com.example.flab.soft.shoppingmallfashion.address.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.flab.soft.shoppingmallfashion.auth.jwt.TokenProvider;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.dto.TokenBuildDto;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AddressAddControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenProvider tokenProvider;

    String accessToken;

    static final AddressAddRequest BAD_ADDRESS_ADD_REQUEST = AddressAddRequest.builder()
            .recipientName("홍길동")
            .roadAddress("대한로111")
            .addressDetail("101동 101호")
            .zipcode(0)
            .recipientCellphone("01012345678")
            .build();

    static final AddressAddRequest ADDRESS_ADD_REQUEST = AddressAddRequest.builder()
            .recipientName("홍길동")
            .roadAddress("대한로111")
            .addressDetail("101동 101호")
            .zipcode(12345)
            .recipientCellphone("01012345678")
            .build();

    @BeforeEach
    void setUp() {
        User saved = userRepository.save(User.builder()
                .email("testUser@gmail.com")
                .password("TestUser1#")
                .realName("testUser")
                .cellphoneNumber("01012345678")
                .nickname("testUser")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .withdrawal(false)
                .build());

        initToken(saved);
    }

    @Test
    @DisplayName("형식이 잘못된 주소지 추가시 400 응답")
    void whenInvalidAddress_thenReturn400() throws Exception {
        mockMvc.perform(
            post("/api/v1/address")
                    .header("Authorization", accessToken)
                    .content(objectMapper.writeValueAsString(BAD_ADDRESS_ADD_REQUEST))
                    .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").value(ErrorEnum.INVALID_REQUEST.getCode()));
    }

    @Test
    @DisplayName("형식이 맞는 주소지 추가시 400 응답")
    void whenValidAddress_thenReturn400() throws Exception {
        mockMvc.perform(
                        post("/api/v1/address")
                                .header("Authorization", accessToken)
                                .content(objectMapper.writeValueAsString(ADDRESS_ADD_REQUEST))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200));
    }

    private void initToken(User saved) {
        String token = tokenProvider.createAccessToken(TokenBuildDto.builder()
                .subject(saved.getEmail())
                .claim("id", saved.getId())
                .build());

        accessToken = "Bearer " + token;
    }
}