package com.example.flab.soft.shoppingmallfashion.store;

import static org.assertj.core.api.Assertions.*;

import com.example.flab.soft.shoppingmallfashion.store.controller.StoreRegisterRequest;
import com.example.flab.soft.shoppingmallfashion.store.repository.NewStoreRegisterRequestRepository;
import com.example.flab.soft.shoppingmallfashion.store.service.NewStoreRegisterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StoreIntegrationTest {
    @Autowired
    private NewStoreRegisterService newStoreRegisterService;
    @Autowired
    private NewStoreRegisterRequestRepository newStoreRegisterRequestRepository;

    @AfterEach
    void tearDown() {
        newStoreRegisterRequestRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("상점 등록 요청")
    void requestStoreRegistration() {
        //given
        StoreRegisterRequest registerRequest = StoreRegisterRequest.builder()
                .requesterName("name")
                .requesterEmail("email@email.com")
                .requesterPhoneNumber("01012345678")
                .businessRegistrationNumber("0123456789")
                .build();
        //when
        newStoreRegisterService.registerStore(registerRequest);
        //then
        assertThat(newStoreRegisterRequestRepository.count()).isEqualTo(1);
    }
}
