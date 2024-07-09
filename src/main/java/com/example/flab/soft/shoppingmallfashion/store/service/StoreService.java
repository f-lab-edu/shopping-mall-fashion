package com.example.flab.soft.shoppingmallfashion.store.service;

import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.store.controller.AddStoreRequest;
import com.example.flab.soft.shoppingmallfashion.store.repository.Store;
import com.example.flab.soft.shoppingmallfashion.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    public void addStore(AddStoreRequest addStoreRequest, Long id) throws ApiException{
        if (storeRepository.existsByName(addStoreRequest.getName())) {
            throw new ApiException(ErrorEnum.STORE_NAME_DUPLICATED);
        }
        storeRepository.save(Store.builder()
                .name(addStoreRequest.getName())
                .logo(addStoreRequest.getLogo())
                .description(addStoreRequest.getDescription())
                .businessRegistrationNumber(addStoreRequest.getBusinessRegistrationNumber())
                .managerId(id)
                .build());
    }
}
