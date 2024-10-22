package com.example.flab.soft.shoppingmallfashion.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetailsService.CrewAuthService;
import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetailsService.UserAuthService;
import com.example.flab.soft.shoppingmallfashion.store.repository.CrewRepository;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class AuthIntegrationTest {
    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private CrewAuthService crewAuthService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CrewRepository crewRepository;
    String storeManagerEmail;
    String userEmail;

    @BeforeEach
    void setUp() {
        userEmail = userRepository.findById(1L).get().getEmail();
        storeManagerEmail = crewRepository.findById(1L).get().getEmail();
    }

    @Test
    @DisplayName("일반 유저는 유저 권한 하나만 갖는다")
    void normalUserOnlyHaveRoleUser() {
        UserDetails userDetails = userAuthService.loadUserByUsername(userEmail);
        assertThat(userDetails.getAuthorities().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("직원으로 등록된 경우 연관된 권한들을 찾는다")
    void crewHasRelatedAuthorities() {
        UserDetails userDetails = crewAuthService.loadUserByUsername(storeManagerEmail);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertThat(authorities.size()).isEqualTo(4);
    }
}
