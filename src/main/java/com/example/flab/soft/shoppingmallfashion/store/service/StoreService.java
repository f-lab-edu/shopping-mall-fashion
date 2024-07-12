package com.example.flab.soft.shoppingmallfashion.store.service;

import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.store.controller.AddStoreRequest;
import com.example.flab.soft.shoppingmallfashion.store.repository.Store;
import com.example.flab.soft.shoppingmallfashion.store.repository.StoreRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    @Transactional
    public void addStore(AddStoreRequest addStoreRequest, Long id) throws ApiException {
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

    public StoreDto getUserStore(Long userId) throws ApiException {
        Store store = storeRepository.findByManagerId(userId)
                .orElseThrow(() -> new ApiException(ErrorEnum.STORE_NOT_FOUND));
        return buildStoreDto(store);
    }

    @Transactional
    public StoreDto updateMyStore(StoreUpdateDto storeUpdateDto, Long userId) throws ApiException {
        String name = storeUpdateDto.getName();
        if (name != null && storeRepository.existsByName(name)) {
            throw new ApiException(ErrorEnum.STORE_NAME_DUPLICATED);
        }
        Store store = storeRepository.findByManagerId(userId)
                .orElseThrow(() -> new ApiException(ErrorEnum.STORE_NOT_FOUND));
        store.update(storeUpdateDto);
        return buildStoreDto(store);
    }

    @Transactional
    public StoreDto stoppage(Long userId) {
        Store store = storeRepository.findByManagerId(userId)
                .orElseThrow(() -> new ApiException(ErrorEnum.STORE_NOT_FOUND));
        store.beOnStoppage();
        return buildStoreDto(store);
    }

    private StoreDto buildStoreDto(Store store) {
        return StoreDto.builder()
                .id(store.getId())
                .name(store.getName())
                .logo(store.getLogo())
                .description(store.getDescription())
                .businessRegistrationNumber(store.getBusinessRegistrationNumber())
                .saleState(store.getSaleState().name())
                .build();
    }
}
