package com.example.flab.soft.shoppingmallfashion.user.service;

import com.example.flab.soft.shoppingmallfashion.user.controller.UserSignUpDto;
import com.example.flab.soft.shoppingmallfashion.user.domain.UserEntity;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import com.example.flab.soft.shoppingmallfashion.user.service.exception.fieldConflict.SignUpCellphoneNumberConflictException;
import com.example.flab.soft.shoppingmallfashion.user.service.exception.fieldConflict.SignUpEmailConflictException;
import com.example.flab.soft.shoppingmallfashion.user.service.exception.fieldConflict.SignUpIdConflictException;
import com.example.flab.soft.shoppingmallfashion.user.service.exception.fieldConflict.SignUpNicknameConflictException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void signUp(UserSignUpDto userSignUpDto) {
        checkFieldTypes(userSignUpDto);
        checkDuplication(userSignUpDto);
        userRepository.save(
                UserEntity.builder()
                        .signinId(userSignUpDto.getSigninId())
                        .password(userSignUpDto.getPassword())
                        .name(userSignUpDto.getName())
                        .email(userSignUpDto.getEmail())
                        .cellphoneNumber(userSignUpDto.getCellphoneNumber())
                        .nickname(userSignUpDto.getNickname())
                        .createdAt(LocalDate.now())
                        .build()
        );
    }

    private void checkFieldTypes(UserSignUpDto userSignUpDto) {
        UserFieldType.ID.check(userSignUpDto.getSigninId());
        UserFieldType.PASSWORD.check(userSignUpDto.getPassword());
        UserFieldType.EMAIL.check(userSignUpDto.getEmail());
        UserFieldType.CELLPHONE.check(userSignUpDto.getCellphoneNumber());
        UserFieldType.NICKNAME.check(userSignUpDto.getNickname());
    }

    private void checkDuplication(UserSignUpDto userSignUpDto) {
        if (userRepository.existsByUserSigninInfo_SigninId(userSignUpDto.getSigninId())) {
            throw new SignUpIdConflictException();
        }
        if (userRepository.existsByEmail(userSignUpDto.getEmail())) {
            throw new SignUpEmailConflictException();
        }
        if (userRepository.existsByCellphoneNumber(userSignUpDto.getSigninId())) {
            throw new SignUpCellphoneNumberConflictException();
        }
        if (userRepository.existsByNickname(userSignUpDto.getSigninId())) {
            throw new SignUpNicknameConflictException();
        }
    }
}
