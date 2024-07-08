package com.example.flab.soft.shoppingmallfashion.user.service;

import com.example.flab.soft.shoppingmallfashion.auth.role.Authority;
import com.example.flab.soft.shoppingmallfashion.auth.role.RoleService;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.user.controller.UserSignUpRequest;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(UserSignUpRequest userSignUpRequest) {
        checkDuplication(userSignUpRequest);
        saveUser(userSignUpRequest);
    }

    private void saveUser(UserSignUpRequest userSignUpRequest) {
        User user = userRepository.save(
                User.builder()
                        .email(userSignUpRequest.getEmail())
                        .password(passwordEncoder.encode(userSignUpRequest.getPassword()))
                        .realName(userSignUpRequest.getRealName())
                        .cellphoneNumber(userSignUpRequest.getCellphoneNumber())
                        .nickname(userSignUpRequest.getNickname())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );

        roleService.save(user, Authority.ROLE_USER);
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

    @Transactional
    public UserDto updateEmail(Long id, String email) {
        User user = userRepository.findById(id).orElseThrow();
        return user.changeEmail(email);
    }

    @Transactional
    public UserDto updateRealName(Long id, String realName) {
        User user = userRepository.findById(id).orElseThrow();
        return user.changeRealName(realName);
    }

    @Transactional
    public UserDto updateCellphone(Long id, String cellphoneNumber) {
        User user = userRepository.findById(id).orElseThrow();
        return user.changeCellphone(cellphoneNumber);
    }
}
