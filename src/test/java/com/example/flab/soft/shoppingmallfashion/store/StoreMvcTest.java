package com.example.flab.soft.shoppingmallfashion.store;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.flab.soft.shoppingmallfashion.WithMockCustomUser;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.store.controller.StoreRegisterRequest;
import com.example.flab.soft.shoppingmallfashion.store.controller.StoreController;
import com.example.flab.soft.shoppingmallfashion.store.service.CrewService;
import com.example.flab.soft.shoppingmallfashion.store.service.NewStoreRegisterService;
import com.example.flab.soft.shoppingmallfashion.store.service.StoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StoreController.class)
@WithMockCustomUser
class StoreMvcTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private StoreService storeService;
    @MockBean
    private CrewService crewService;
    @MockBean
    private NewStoreRegisterService newStoreRegisterService;

    @Test
    @DisplayName("사업자 등록번호는 10자리 숫자")
    void businessRegistrationNumberIs10Digit_ifNotReturn400() throws Exception {
        mvc.perform(
                        post("/api/v1/store/register")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .content(mapper.writeValueAsString(StoreRegisterRequest.builder()
                                        .requesterName("name")
                                        .requesterEmail("test@email.com")
                                        .requesterPhoneNumber("01012345678")
                                        .businessRegistrationNumber("123")
                                        .build()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").value(ErrorEnum.INVALID_REQUEST.getCode()));
    }

    @Test
    @DisplayName("이름은 공백일 수 없다.")
    void nameCannotBeBlank_ifNotReturn400() throws Exception {
        mvc.perform(
                        post("/api/v1/store/register")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .content(mapper.writeValueAsString(StoreRegisterRequest.builder()
                                        .requesterName("name")
                                        .requesterEmail("test@email.com")
                                        .requesterPhoneNumber("01012345678")
                                        .businessRegistrationNumber("123")
                                        .build()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").value(ErrorEnum.INVALID_REQUEST.getCode()));
    }

    @Test
    @DisplayName("기존에 존재하는 정보로 상점 등록 요청시 기존 정보를 쓴다.")
    void whenRegisterWithDuplicatedName_thenReturn409() throws Exception {
        // TODO
    }
}