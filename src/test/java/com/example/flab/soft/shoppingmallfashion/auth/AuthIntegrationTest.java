package com.example.flab.soft.shoppingmallfashion.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetailsService.CrewAuthService;
import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetailsService.UserAuthService;
import java.util.Collection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SpringBootTest
public class AuthIntegrationTest {
    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private CrewAuthService crewAuthService;
    @Value("${authorization.store-manager.email}")
    String storeManagerEmail;
    @Value("${authorization.user.email}")
    String userEmail;

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
