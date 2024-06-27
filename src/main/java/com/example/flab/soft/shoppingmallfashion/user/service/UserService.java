package com.example.flab.soft.shoppingmallfashion.user.service;

import com.example.flab.soft.shoppingmallfashion.auth.Role;
import com.example.flab.soft.shoppingmallfashion.auth.RoleRepository;
import com.example.flab.soft.shoppingmallfashion.auth.UserRole;
import com.example.flab.soft.shoppingmallfashion.auth.UserRoleRepository;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.user.controller.UserSignUpRequest;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
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
                        .createdAt(LocalDate.now())
                        .build()
        );

        Role role = roleRepository.findByAuthority("ROLE_USER");

        userRoleRepository.save(
                UserRole.builder()
                        .role(role)
                        .userId(user.getId())
                        .build());
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
}
