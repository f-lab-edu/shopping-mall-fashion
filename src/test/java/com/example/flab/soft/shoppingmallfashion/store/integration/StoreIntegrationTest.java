package com.example.flab.soft.shoppingmallfashion.store.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.store.controller.AddStoreRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
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

    @Test
    @DisplayName("상점 정보 수정")
    void updateMyStore() throws Exception {
        mvc.perform(
                        patch("/api/v1/store/myStore?type=name")
                                .header("Authorization", managerToken)
                                .content(mapper.writeValueAsString(Map.of("value", "Store NameChanged")))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.response.name").value("Store NameChanged"));
    }

    @Test
    @DisplayName("이미 존재하는 이름으로 상점 정보 수정시 409 응답")
    void updateMyStoreDuplicatedName_thenReturn409() throws Exception {
        mvc.perform(
                        patch("/api/v1/store/myStore?type=name")
                                .header("Authorization", managerToken)
                                .content(mapper.writeValueAsString(Map.of("value", "Store Two")))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(409))
                .andExpect(jsonPath("$.code").value(ErrorEnum.STORE_NAME_DUPLICATED.getCode()));
    }

    @Test
    @DisplayName("상점 휴업")
    void store_stoppage() throws Exception {
        mvc.perform(
                        patch("/api/v1/store/myStore/stoppage")
                                .header("Authorization", managerToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.response.state").value("ON_STOPPAGE"));
    }
}
