package com.example.flab.soft.shoppingmallfashion.address.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.flab.soft.shoppingmallfashion.address.repository.Address;
import com.example.flab.soft.shoppingmallfashion.address.repository.AddressRepository;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.TokenProvider;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AddressReadControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    TokenProvider tokenProvider;


    @Value("${authorization.user.token}")
    String accessToken;
    @Value("${authorization.user.id}")
    Long userId;
    Address savedAddress;

    @BeforeEach
    void setUp() {
        Address address = Address.builder()
                .recipientName("주소 조회 홍길동")
                .roadAddress("대한로111")
                .addressDetail("101동 101호")
                .zipcode(12345)
                .recipientCellphone("01012345678")
                .userId(userId)
                .build();

        savedAddress = addressRepository.save(address);
    }

    @Test
    @DisplayName("주소록 조회")
    void readAddresses() throws Exception {
        mockMvc.perform(
                        get("/api/v1/address")
                                .header("Authorization", accessToken)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.response.addresses[?(@.recipientName == '주소 조회 홍길동')]").exists());
    }
}