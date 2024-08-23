package com.example.flab.soft.shoppingmallfashion.address;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.flab.soft.shoppingmallfashion.WithMockCustomUser;
import com.example.flab.soft.shoppingmallfashion.address.controller.AddressAddRequest;
import com.example.flab.soft.shoppingmallfashion.address.controller.AddressController;
import com.example.flab.soft.shoppingmallfashion.address.service.AddressService;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AddressController.class)
@WithMockCustomUser(authorities = "STORE_MANAGEMENT")
class AddressControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AddressService addressService;

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

    @Test
    @DisplayName("형식이 잘못된 주소지 추가시 400 응답")
    void whenInvalidAddress_thenReturn400() throws Exception {
        mockMvc.perform(
            post("/api/v1/address")
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
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
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .content(objectMapper.writeValueAsString(ADDRESS_ADD_REQUEST))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200));
    }
}