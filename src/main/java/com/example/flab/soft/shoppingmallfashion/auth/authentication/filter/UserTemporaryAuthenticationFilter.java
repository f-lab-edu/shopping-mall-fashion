package com.example.flab.soft.shoppingmallfashion.auth.authentication.filter;

import com.example.flab.soft.shoppingmallfashion.auth.jwt.TempLoginRequest;
import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetails.AuthUser;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.TokenProvider;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.dto.TempTokenDto;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.dto.TokenBuildDto;
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
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

public class UserTemporaryAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final ObjectMapper objectMapper;
    private final TokenProvider tokenProvider;
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/users/temporary-login", "POST");

    public UserTemporaryAuthenticationFilter(ObjectMapper objectMapper, TokenProvider tokenProvider) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.tokenProvider = tokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        TempLoginRequest tempLoginRequest = parsePhoneNumber(request);
        AbstractAuthenticationToken token = constructAuthenticationToken(tempLoginRequest);
        return super.getAuthenticationManager().authenticate(token);
    }

    protected AbstractAuthenticationToken constructAuthenticationToken(TempLoginRequest tempLoginRequest) {
        return new UserAuthenticationToken(
                tempLoginRequest.getPhoneNumber(), tempLoginRequest.getPhoneNumber());
    }

    private TempLoginRequest parsePhoneNumber(HttpServletRequest request) {
        TempLoginRequest tempLoginRequest;
        try {
            String requestString = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            tempLoginRequest = objectMapper.readValue(requestString, TempLoginRequest.class);
        } catch (IOException e) {
            throw new ApiException(ErrorEnum.FORBIDDEN_REQUEST);
        }
        return tempLoginRequest;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        AuthUser authUser = (AuthUser) authResult.getPrincipal();

        String successResult = objectMapper.writeValueAsString(
                SuccessResult.builder()
                        .response(TempTokenDto.builder()
                                .accessToken(tokenProvider.createAccessToken(extractTokenBuildData(authUser)))
                                .build())
                        .build());
        response.getWriter().write(successResult);
    }

    private TokenBuildDto extractTokenBuildData(AuthUser authUser) {
        return TokenBuildDto.builder()
                .subject(authUser.getRole() + " " + authUser.getEmail())
                .build();
    }
}
