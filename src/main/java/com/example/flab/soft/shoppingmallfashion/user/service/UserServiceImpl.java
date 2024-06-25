package com.example.flab.soft.shoppingmallfashion.user.service;

import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.user.controller.UserSignUpInfo;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUp(UserSignUpInfo userSignUpInfo) {
        checkDuplication(userSignUpInfo);
        saveUser(userSignUpInfo);
    }

    private void saveUser(UserSignUpInfo userSignUpInfo) {
        userRepository.save(
                User.builder()
                        .username(userSignUpInfo.getUsername())
                        .password(passwordEncoder.encode(userSignUpInfo.getPassword()))
                        .realName(userSignUpInfo.getRealName())
                        .email(userSignUpInfo.getEmail())
                        .cellphoneNumber(userSignUpInfo.getCellphoneNumber())
                        .nickname(userSignUpInfo.getNickname())
                        .createdAt(LocalDate.now())
                        .build()
        );
    }

    private void checkDuplication(UserSignUpInfo userSignUpInfo) {
        if (userRepository.existsByUsername(userSignUpInfo.getUsername())) {
            throw new ApiException(ErrorEnum.USERNAME_DUPLICATED);
        }
        if (userRepository.existsByEmail(userSignUpInfo.getEmail())) {
            throw new ApiException(ErrorEnum.EMAIL_DUPLICATED);
        }
        if (userRepository.existsByCellphoneNumber(userSignUpInfo.getCellphoneNumber())) {
            throw new ApiException(ErrorEnum.CELLPHONE_DUPLICATED);
        }
        if (userRepository.existsByNickname(userSignUpInfo.getNickname())) {
            throw new ApiException(ErrorEnum.NICKNAME_DUPLICATED);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException((username)));
    }
}
