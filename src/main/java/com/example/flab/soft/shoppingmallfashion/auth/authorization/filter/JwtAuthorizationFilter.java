package com.example.flab.soft.shoppingmallfashion.auth.authorization.filter;

import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetailsService.CrewAuthService;
import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetailsService.UserAuthService;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.Role;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.TokenProvider;
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
    private final UserAuthService userAuthService;
    private final CrewAuthService crewAuthService;
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserAuthService userAuthService,
                                  CrewAuthService crewAuthService,
                                  TokenProvider tokenProvider, ObjectMapper objectMapper) {
        super(authenticationManager);
        this.userAuthService = userAuthService;
        this.crewAuthService = crewAuthService;
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
            String subject = tokenProvider.getSubject(jwtToken);
            Role role = Role.valueOf(parseRole(subject));
            String username = parseUsername(subject);

            UserDetails user = null;
            switch (role) {
                case USER -> user = userAuthService.loadUserByUsername(username);
                case CREW -> user = crewAuthService.loadUserByUsername(username);
            }

            if (user != null && !user.isEnabled()) {
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

    private String parseUsername(String subject) {
        return subject.split(" ")[1];
    }

    private String parseRole(String subject) {
        return subject.split(" ")[0];
    }

    private String parseToken(HttpServletRequest request) {
        return parseUsername(request.getHeader(AUTHORIZATION_HEADER));
    }
}
