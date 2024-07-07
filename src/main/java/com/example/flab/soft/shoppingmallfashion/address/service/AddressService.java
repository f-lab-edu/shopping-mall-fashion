package com.example.flab.soft.shoppingmallfashion.address.service;

import com.example.flab.soft.shoppingmallfashion.address.controller.AddressAddRequest;
import com.example.flab.soft.shoppingmallfashion.address.repository.Address;
import com.example.flab.soft.shoppingmallfashion.address.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    public void add(AddressAddRequest request, Long userId) {
        addressRepository.save(toAddressEntity(request, userId));
    }

    private Address toAddressEntity(AddressAddRequest request, Long userId) {
        return Address.builder()
                .recipientName(request.getRecipientName())
                .roadAddress(request.getRoadAddress())
                .addressDetail(request.getAddressDetail())
                .zipcode(request.getZipcode())
                .recipientCellphone(request.getRecipientCellphone())
                .userId(userId)
                .build();
    }
}
