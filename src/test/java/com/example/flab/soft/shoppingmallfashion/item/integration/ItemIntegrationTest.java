package com.example.flab.soft.shoppingmallfashion.item.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.flab.soft.shoppingmallfashion.item.controller.ItemCreateRequest;
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
public class ItemIntegrationTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Value("${authorization.user.token}")
    String userToken;
    @Value("${authorization.admin.token}")
    String itemManagerToken;

    @Test
    @DisplayName("상품 관리자 권한이 없이 접근시 403 에러")
    void withoutItemManagerAuthority_thenReturn403() throws Exception {
        mockMvc.perform(
                        post("/api/v1/item/management/new-item")
                                .header("Authorization", itemManagerToken)
                                .content(mapper.writeValueAsString(ItemCreateRequest.builder()
                                        .name("name")
                                        .price(1000)
                                        .sex("UNISEX")
                                        .saleState("PREPARING")
                                        .storeId(1L)
                                        .categoryId(1L)
                                        .build()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        mockMvc.perform(
                post("/api/v1/item/management/new-item")
                        .header("Authorization", userToken)
                        .content(mapper.writeValueAsString(ItemCreateRequest.builder()
                                .name("name")
                                .price(1000)
                                .sex("UNISEX")
                                .storeId(1L)
                                .categoryId(1L)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403));
    }

    @Test
    @DisplayName("상품 등록시 필수값이 존재하지 않으면 400에러")
    void whenMandatoryFieldIsBlank_thenReturn400() throws Exception {
        mockMvc.perform(
                        post("/api/v1/item/management/new-item")
                                .header("Authorization", itemManagerToken)
                                .content(mapper.writeValueAsString(ItemCreateRequest.builder()
                                        .name("  ")
                                        .price(1000)
                                        .sex("UNISEX")
                                        .saleState("PREPARING")
                                        .storeId(1L)
                                        .categoryId(1L)
                                        .build()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }
}
