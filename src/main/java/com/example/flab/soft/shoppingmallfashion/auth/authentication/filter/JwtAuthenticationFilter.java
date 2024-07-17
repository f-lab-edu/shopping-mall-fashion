package com.example.flab.soft.shoppingmallfashion.auth.authentication.filter;

import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetails.AuthUser;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.LoginRequest;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.dto.NewTokensDto;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.dto.TokenBuildDto;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.dto.TokenResponse;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.dto.TokensMapper;
import com.example.flab.soft.shoppingmallfashion.auth.refreshToken.RefreshTokenService;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StreamUtils;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final RefreshTokenService refreshTokenService;
    private final ObjectMapper objectMapper;
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/users/login",
            "POST");

    public JwtAuthenticationFilter(ObjectMapper objectMapper, RefreshTokenService refreshTokenService) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.objectMapper = objectMapper;
        this.refreshTokenService = refreshTokenService;
    }

    public JwtAuthenticationFilter(
            RequestMatcher requiresAuthenticationRequestMatcher,
            RefreshTokenService refreshTokenService, ObjectMapper objectMapper) {
        super(requiresAuthenticationRequestMatcher);
        this.refreshTokenService = refreshTokenService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        LoginRequest loginRequest = parseUsernamePassword(request);
        UsernamePasswordAuthenticationToken token = constructAuthenticationToken(loginRequest);
        return super.getAuthenticationManager().authenticate(token);
    }

    protected UsernamePasswordAuthenticationToken constructAuthenticationToken(LoginRequest loginRequest) {
        return new UserAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword());
    }

    private LoginRequest parseUsernamePassword(HttpServletRequest request) {
        LoginRequest loginRequest;
        try {
            String requestString = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            loginRequest = objectMapper.readValue(requestString, LoginRequest.class);
        } catch (IOException e) {
            throw new ApiException(ErrorEnum.FORBIDDEN_REQUEST);
        }
        return loginRequest;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        AuthUser authUser = (AuthUser) authResult.getPrincipal();

        NewTokensDto newTokensDto = refreshTokenService.getNewToken(extractTokenBuildData(authUser));
        TokenResponse tokenResponse = TokensMapper.INSTANCE.toTokenResponseDto(newTokensDto);
        String successResult = objectMapper.writeValueAsString(
                SuccessResult.builder().response(tokenResponse).build());
        response.getWriter().write(successResult);
    }

    private TokenBuildDto extractTokenBuildData(AuthUser authUser) {
        return TokenBuildDto.builder()
                .subject(authUser.getRole() + " " + authUser.getEmail())
                .build();
    }
}
