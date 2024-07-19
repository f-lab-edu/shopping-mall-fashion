package com.example.flab.soft.shoppingmallfashion.auth.authentication.provider;

import com.example.flab.soft.shoppingmallfashion.auth.authentication.filter.UserAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserAuthenticationProvider extends DaoAuthenticationProvider {
    private final UserDetailsService userAuthService;

    public UserAuthenticationProvider(PasswordEncoder passwordEncoder,
                                      UserDetailsService userAuthService) {
        super(passwordEncoder);
        this.userAuthService = userAuthService;
    }

    @Override
    protected UserDetailsService getUserDetailsService() {
        return userAuthService;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UserAuthenticationToken.class.isAssignableFrom(authentication));
    }
}

