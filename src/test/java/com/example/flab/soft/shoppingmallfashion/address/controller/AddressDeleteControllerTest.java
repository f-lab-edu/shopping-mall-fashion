package com.example.flab.soft.shoppingmallfashion.address.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.flab.soft.shoppingmallfashion.address.repository.Address;
import com.example.flab.soft.shoppingmallfashion.address.repository.AddressRepository;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.TokenProvider;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.dto.TokenBuildDto;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AddressDeleteControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    AddressRepository addressRepository;

    String accessToken;
    Address savedAddress;
    Address savedAddressOfOtherUser;

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
        User savedUser = userRepository.save(User.builder()
                .email("testUser@gmail.com")
                .password("TestUser1#")
                .realName("testUser")
                .cellphoneNumber("01012345678")
                .nickname("testUser")
                .createdAt(LocalDate.now())
                .build());

        User savedUser2 = userRepository.save(User.builder()
                .email("testUser2@gmail.com")
                .password("TestUser2#")
                .realName("testUser")
                .cellphoneNumber("01012345679")
                .nickname("testUser2")
                .createdAt(LocalDate.now())
                .build());

        initToken(savedUser);

        Address address = Address.builder()
                .recipientName("홍길동")
                .roadAddress("대한로111")
                .addressDetail("101동 101호")
                .zipcode(12345)
                .recipientCellphone("01012345678")
                .userId(savedUser.getId())
                .build();

        Address addressOfOtherUser = Address.builder()
                .recipientName("안정환")
                .roadAddress("대한로123")
                .addressDetail("101동 101호")
                .zipcode(12346)
                .recipientCellphone("01012345678")
                .userId(savedUser2.getId())
                .build();

        savedAddress = addressRepository.save(address);
        savedAddressOfOtherUser = addressRepository.save(addressOfOtherUser);
    }

    @Test
    @DisplayName("유저의 존재하지 않는 주소지 삭제시 400 에러")
    void whenDeleteNotExistingAddressOfUser_thenReturn400() throws Exception {
        mockMvc.perform(
                        delete("/api/v1/address/" + 10000)
                                .header("Authorization", accessToken)
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").value(ErrorEnum.NO_SUCH_ADDRESS.getCode()));
    }

    @Test
    @DisplayName("유저의 존재하는 주소지 삭제시 200 응답")
    void whenDeleteExistingAddressOfUser_thenReturn200() throws Exception {
        mockMvc.perform(
                        delete("/api/v1/address/" + savedAddress.getId())
                                .header("Authorization", accessToken)
                )
                .andExpect(status().is(200));
    }

    @Test
    @DisplayName("다른 유저의 주소지 삭제시 403 에러")
    void whenDeleteOtherUsersAddress_thenReturn403() throws Exception {
        mockMvc.perform(
                        delete("/api/v1/address/" + savedAddressOfOtherUser.getId())
                                .header("Authorization", accessToken)
                )
                .andExpect(status().is(403))
                .andExpect(jsonPath("$.code").value(ErrorEnum.FORBIDDEN_ADDRESS_REQUEST.getCode()));
    }

    private void initToken(User saved) {
        String token = tokenProvider.createAccessToken(TokenBuildDto.builder()
                .subject(saved.getEmail())
                .claim("id", saved.getId())
                .build());

        accessToken = "Bearer " + token;
    }
}