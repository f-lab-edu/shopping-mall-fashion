package com.example.flab.soft.shoppingmallfashion.user.repository;

import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByCellphoneNumber(String cellphoneNumber);
    boolean existsByNickname(String nickname);
    Optional<User> findByEmail(String email);
}
