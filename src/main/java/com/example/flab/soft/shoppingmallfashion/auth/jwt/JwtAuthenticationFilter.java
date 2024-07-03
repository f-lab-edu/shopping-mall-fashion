package com.example.flab.soft.shoppingmallfashion.auth.jwt;

import com.example.flab.soft.shoppingmallfashion.auth.AuthUser;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.dto.TokenBuildDto;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.dto.TokensDto;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.refreshToken.RefreshTokenService;
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
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final RefreshTokenService refreshTokenService;
    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        LoginRequest loginRequest;
        try {
            String requestString = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            loginRequest = objectMapper.readValue(requestString, LoginRequest.class);
        } catch (IOException e) {
            throw new ApiException(ErrorEnum.FORBIDDEN_REQUEST);
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword());
        return super.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        AuthUser authUser = (AuthUser) authResult.getPrincipal();
        TokenBuildDto tokenBuildDto = extractTokenBuildData(authUser);
        TokensDto tokensDto = refreshTokenService.getNewToken(tokenBuildDto);

        TokenResponse tokenResponse = buildTokenResponse(tokensDto);
        String successResult = objectMapper.writeValueAsString(
                SuccessResult.builder().response(tokenResponse).build());

        response.getWriter().write(successResult);
    }

    private TokenBuildDto extractTokenBuildData(AuthUser authUser) {
        return TokenBuildDto.builder()
                .subject(authUser.getEmail())
                .claim("id", authUser.getId())
                .build();
    }

    private TokenResponse buildTokenResponse(TokensDto tokensDto) {
        return TokenResponse.builder()
                .accessToken(tokensDto.getAccessToken())
                .refreshToken(tokensDto.getRefreshToken())
                .build();
    }
}
