package com.example.flab.soft.shoppingmallfashion.address.service;

import com.example.flab.soft.shoppingmallfashion.address.controller.AddressAddRequest;
import com.example.flab.soft.shoppingmallfashion.address.repository.Address;
import com.example.flab.soft.shoppingmallfashion.address.repository.AddressRepository;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    @Transactional
    public void add(AddressAddRequest request, Long userId) {
        addressRepository.save(toAddressEntity(request, userId));
    }

    @Transactional
    public void delete(Long addressId, Long userId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ApiException(ErrorEnum.NO_SUCH_ADDRESS));

        if (address.getUserId() != userId) {
            throw new ApiException(ErrorEnum.FORBIDDEN_ADDRESS_REQUEST);
        }
        addressRepository.delete(address);
    }

    public Addresses getAddresses(Long userId) {
        List<Address> addresses = addressRepository.findAllByUserId(userId);
        return Addresses.builder().addresses(addresses).build();
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
