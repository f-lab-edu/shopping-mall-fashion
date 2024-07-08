package com.example.flab.soft.shoppingmallfashion.auth.jwt;

import com.example.flab.soft.shoppingmallfashion.auth.AuthService;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final AuthService authService;
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, AuthService authService,
                                  TokenProvider tokenProvider, ObjectMapper objectMapper) {
        super(authenticationManager);
        this.authService = authService;
        this.tokenProvider = tokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(AUTHORIZATION_HEADER);

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String jwtToken = parseToken(request);
        if (jwtToken != null && tokenProvider.validateToken(jwtToken)) {
            String subject = tokenProvider.getSubjectFromToken(jwtToken);

            UserDetails user = authService.loadUserByUsername(subject);
            if (!user.isEnabled()) {
                response.getWriter().write(
                        objectMapper.writeValueAsString(
                                new ErrorResult(ErrorEnum.INACTIVATED_USER)));
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user, user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private String parseToken(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_HEADER).split(" ")[1];
    }
}
