package com.example.flab.soft.shoppingmallfashion.auth.authentication.filter;

import com.example.flab.soft.shoppingmallfashion.auth.jwt.LoginRequest;
import com.example.flab.soft.shoppingmallfashion.auth.refreshToken.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class CrewAuthenticationFilter extends JwtAuthenticationFilter {
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/crews/login",
            "POST");

    public CrewAuthenticationFilter(ObjectMapper objectMapper,
                                    RefreshTokenService refreshTokenService) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, refreshTokenService, objectMapper);
    }

    @Override
    protected UsernamePasswordAuthenticationToken constructAuthenticationToken(LoginRequest loginRequest) {
        return new CrewAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword());
    }
}
