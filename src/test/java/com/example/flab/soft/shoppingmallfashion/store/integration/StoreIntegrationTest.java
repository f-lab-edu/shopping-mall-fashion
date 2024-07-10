package com.example.flab.soft.shoppingmallfashion.store.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.flab.soft.shoppingmallfashion.store.controller.AddStoreRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class StoreIntegrationTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @Value("${authorization.user.token}")
    String userToken;
    @Value("${authorization.admin.token}")
    String managerToken;
    static final AddStoreRequest ADD_STORE_REQUEST = AddStoreRequest.builder()
            .name("store")
            .logo("logo")
            .description("description")
            .businessRegistrationNumber("0123456789")
            .build();

    @Test
    @DisplayName("상점 등록시 상점 관리자 권한 획득")
    void whenRegisterWithDuplicatedName_thenReturn409() throws Exception {
        mvc.perform(
                        post("/api/v1/store/register")
                                .header("Authorization", userToken)
                                .content(mapper.writeValueAsString(ADD_STORE_REQUEST))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        mvc.perform(
                        get("/api/v1/store/myStore")
                                .header("Authorization", userToken))
                .andExpect(status().is(200));
    }

    @Test
    @DisplayName("상점 정보 조회")
    void getMyStoreInfo() throws Exception {
        mvc.perform(
                        get("/api/v1/store/myStore")
                                .header("Authorization", managerToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.response.name").value("Store One"));
    }
}
