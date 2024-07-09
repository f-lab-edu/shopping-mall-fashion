package com.example.flab.soft.shoppingmallfashion.store.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.store.controller.AddStoreRequest;
import com.example.flab.soft.shoppingmallfashion.store.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {
    @Mock
    private StoreRepository storeRepository;
    @InjectMocks
    private StoreService storeService;

    private static final AddStoreRequest addStoreRequest = AddStoreRequest.builder()
            .name("name")
            .logo("logo")
            .description("description")
            .businessRegistrationNumber("0123456789")
            .build();

    @Test
    @DisplayName("이미 등록된 이름으로 등록시 예외")
    void whenRegisterWithDuplicatedName_throwsException() {
        when(storeRepository.existsByName(anyString())).thenReturn(true);
        assertThrows(ApiException.class, () -> storeService.addStore(addStoreRequest, 1L));
    }
}