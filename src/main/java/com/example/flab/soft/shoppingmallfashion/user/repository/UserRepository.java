package com.example.flab.soft.shoppingmallfashion.user.repository;

import com.example.flab.soft.shoppingmallfashion.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUserSigninInfo_SigninId(String signinId);
    boolean existsByEmail(String email);
    boolean existsByCellphoneNumber(String cellphoneNumber);
    boolean existsByNickname(String nickname);
}
