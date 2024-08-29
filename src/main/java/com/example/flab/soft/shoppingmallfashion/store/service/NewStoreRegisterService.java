package com.example.flab.soft.shoppingmallfashion.store.service;

import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.store.controller.StoreRegisterRequest;
import com.example.flab.soft.shoppingmallfashion.store.repository.NewStoreRegisterRequest;
import com.example.flab.soft.shoppingmallfashion.store.repository.NewStoreRegisterRequestRepository;
import com.example.flab.soft.shoppingmallfashion.store.util.StoreRegisterMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewStoreRegisterService {
    private final NewStoreRegisterRequestRepository newStoreRegisterRequestRepository;

    @Transactional
    public void registerStore(StoreRegisterRequest storeRegisterRequest) throws ApiException {
        newStoreRegisterRequestRepository.save(toEntity(storeRegisterRequest));
    }

    private NewStoreRegisterRequest toEntity(StoreRegisterRequest storeRegisterRequest) {
        return StoreRegisterMapper.INSTANCE.toEntity(storeRegisterRequest);
    }
}
