package com.example.flab.soft.shoppingmallfashion.user.service;

import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.user.controller.UserSignUpRequest;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(UserSignUpRequest userSignUpRequest) {
        checkDuplication(userSignUpRequest);
        saveUser(userSignUpRequest);
    }

    private void saveUser(UserSignUpRequest userSignUpRequest) {
        userRepository.save(
                User.builder()
                        .email(userSignUpRequest.getEmail())
                        .password(passwordEncoder.encode(userSignUpRequest.getPassword()))
                        .realName(userSignUpRequest.getRealName())
                        .cellphoneNumber(userSignUpRequest.getCellphoneNumber())
                        .nickname(userSignUpRequest.getNickname())
                        .build()
        );
    }

    private void checkDuplication(UserSignUpRequest userSignUpRequest) {
        if (userRepository.existsByEmail(userSignUpRequest.getEmail())) {
            throw new ApiException(ErrorEnum.EMAIL_DUPLICATED);
        }
        if (userRepository.existsByCellphoneNumber(userSignUpRequest.getCellphoneNumber())) {
            throw new ApiException(ErrorEnum.CELLPHONE_DUPLICATED);
        }
        if (userRepository.existsByNickname(userSignUpRequest.getNickname())) {
            throw new ApiException(ErrorEnum.NICKNAME_DUPLICATED);
        }
    }

    public UserDto findUser(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return UserDto.builder()
                .email(user.getEmail())
                .realName(user.getRealName())
                .cellphoneNumber(user.getCellphoneNumber())
                .nickname(user.getNickname())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public void changePassword(Long id, String currentPassword, String newPassword) {
        User user = userRepository.findById(id).orElseThrow();
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new ApiException(ErrorEnum.WRONG_PASSWORD);
        }
        user.changePassword(passwordEncoder.encode(newPassword));
    }

    @Transactional
    public UserDto updateField(Long id, String fieldName, String updatedValue) {
        User user = userRepository.findById(id).orElseThrow();
        return user.update(fieldName, updatedValue);
    }

    @Transactional
    public void withdraw(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.withdraw();
    }
}
