package com.example.flab.soft.shoppingmallfashion.auth;

import com.example.flab.soft.shoppingmallfashion.auth.exception.AuthenticationFailureEntryPoint;
import com.example.flab.soft.shoppingmallfashion.auth.exception.AuthorizationFailureHandler;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.JwtAuthenticationFilter;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.JwtAuthorizationFilter;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.TokenProvider;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.refreshToken.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final AuthorizationFailureHandler authorizationFailureHandler;
    private final AuthenticationFailureEntryPoint authenticationFailureEntryPoint;
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf((AbstractHttpConfigurer::disable))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagementCustomizer ->
                        sessionManagementCustomizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers((headersConfigurer) ->
                        headersConfigurer.frameOptions(FrameOptionsConfig::disable))

                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthorizationFilter(), BasicAuthenticationFilter.class)

                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers(PathRequest.toH2Console()).permitAll()
                                .requestMatchers(
                                        "/login",
                                        "/api/v1/users/signup",
                                        "/api/v1/auth/refresh-token").permitAll()
                                .anyRequest().authenticated())

                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling
                                .accessDeniedHandler(authorizationFailureHandler)
                                .authenticationEntryPoint(authenticationFailureEntryPoint));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public UsernamePasswordAuthenticationFilter jwtAuthenticationFilter()
            throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(refreshTokenService, objectMapper);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return jwtAuthenticationFilter;
    }

    @Bean
    public BasicAuthenticationFilter jwtAuthorizationFilter()
            throws Exception {
        return new JwtAuthorizationFilter(
                authenticationManager(authenticationConfiguration), authService, tokenProvider, objectMapper);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
