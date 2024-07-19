package com.example.flab.soft.shoppingmallfashion.store.service;

import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.store.controller.CrewSignUpRequest;
import com.example.flab.soft.shoppingmallfashion.store.repository.Crew;
import com.example.flab.soft.shoppingmallfashion.store.repository.CrewRepository;
import com.example.flab.soft.shoppingmallfashion.store.repository.Store;
import com.example.flab.soft.shoppingmallfashion.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CrewService {
    private final StoreRepository storeRepository;
    private final CrewRepository crewRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void addCrew(CrewSignUpRequest crewSignUpRequest) {
        Store store = storeRepository.findByName(crewSignUpRequest.getStoreName())
                .orElseThrow(() -> new ApiException(ErrorEnum.STORE_NOT_FOUND));

        crewRepository.save(Crew.builder()
                .email(crewSignUpRequest.getEmail())
                .password(passwordEncoder.encode(crewSignUpRequest.getPassword()))
                .cellphoneNumber(crewSignUpRequest.getCellphoneNumber())
                .name(crewSignUpRequest.getName())
                .store(store)
                .build());
    }
}
