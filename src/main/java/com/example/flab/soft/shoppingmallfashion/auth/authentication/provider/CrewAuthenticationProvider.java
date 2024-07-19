package com.example.flab.soft.shoppingmallfashion.auth.authentication.provider;

import com.example.flab.soft.shoppingmallfashion.auth.authentication.filter.CrewAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CrewAuthenticationProvider extends DaoAuthenticationProvider {
    private final UserDetailsService crewAuthService;

    public CrewAuthenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService crewAuthService) {
        super(passwordEncoder);
        this.crewAuthService = crewAuthService;
    }

    @Override
    protected UserDetailsService getUserDetailsService() {
        return crewAuthService;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (CrewAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
