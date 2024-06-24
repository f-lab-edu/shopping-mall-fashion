package com.example.flab.soft.shoppingmallfashion.user.service;

import com.example.flab.soft.shoppingmallfashion.exception.UserException;
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
            throw new UserException("이미 등록된 아이디입니다.");
        }
        if (userRepository.existsByEmail(userSignUpInfo.getEmail())) {
            throw new UserException("이미 등록된 이메일입니다.");
        }
        if (userRepository.existsByCellphoneNumber(userSignUpInfo.getCellphoneNumber())) {
            throw new UserException("이미 등록된 전화번호입니다");
        }
        if (userRepository.existsByNickname(userSignUpInfo.getNickname())) {
            throw new UserException("이미 등록된 닉네임입니다");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException((username)));
    }
}
