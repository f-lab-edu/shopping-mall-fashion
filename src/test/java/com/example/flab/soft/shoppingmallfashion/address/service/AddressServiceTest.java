package com.example.flab.soft.shoppingmallfashion.address.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.flab.soft.shoppingmallfashion.address.repository.Address;
import com.example.flab.soft.shoppingmallfashion.address.repository.AddressRepository;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {
    @Mock
    private AddressRepository addressRepository;
    @InjectMocks
    private AddressService addressService;

    Address address = Address.builder()
            .recipientName("홍길동")
            .roadAddress("대한로111")
            .addressDetail("101동 101호")
            .zipcode(12345)
            .recipientCellphone("01012345678")
            .userId(1L)
            .build();

    @Test
    @DisplayName("다른 유저의 주소를 삭제하려 하면 예외를 던진다.")
    void whenDeleteOtherUsersAddress_thenThrowException() {
        when(addressRepository.findById(any())).thenReturn(Optional.of(address));

        assertThatThrownBy(() -> addressService.delete(1L, 2L)).isInstanceOf(ApiException.class);
    }
}