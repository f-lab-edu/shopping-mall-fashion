package com.example.flab.soft.shoppingmallfashion.store.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.flab.soft.shoppingmallfashion.WithMockCustomUser;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.store.service.StoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StoreController.class)
@WithMockCustomUser
class StoreControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private StoreService storeService;

    @Value("${authorization.user.token}")
    String accessToken;

    @Test
    @DisplayName("사업자 등록번호는 10자리 숫자")
    void businessRegistrationNumberIs10Digit_ifNotReturn400() throws Exception {
        mvc.perform(
                        post("/api/v1/store/register")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .content(mapper.writeValueAsString(AddStoreRequest.builder()
                                        .name("store")
                                        .logo("logo")
                                        .description("description")
                                        .businessRegistrationNumber("123")
                                        .build()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").value(ErrorEnum.INVALID_REQUEST.getCode()));
    }

    @Test
    @DisplayName("상점 이름은 공백일 수 없다.")
    void nameCannotBeBlank_ifNotReturn400() throws Exception {
        mvc.perform(
                        post("/api/v1/store/register")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .content(mapper.writeValueAsString(AddStoreRequest.builder()
                                        .name("")
                                        .logo("logo")
                                        .description("description")
                                        .businessRegistrationNumber("0123456789")
                                        .build()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").value(ErrorEnum.INVALID_REQUEST.getCode()));
    }

    @Test
    @DisplayName("이미 존재하는 이름의 상점 등록시 409에러")
    void whenRegisterWithDuplicatedName_thenReturn409() throws Exception {
        doThrow(new ApiException(ErrorEnum.STORE_NAME_DUPLICATED))
                .when(storeService).registerStore(any(AddStoreRequest.class), anyLong());

        mvc.perform(
                        post("/api/v1/store/register")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .content(mapper.writeValueAsString(AddStoreRequest.builder()
                                        .name("store")
                                        .logo("logo")
                                        .description("description")
                                        .businessRegistrationNumber("0123456789")
                                        .build()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(409))
                .andExpect(jsonPath("$.code").value(ErrorEnum.STORE_NAME_DUPLICATED.getCode()));
    }
}