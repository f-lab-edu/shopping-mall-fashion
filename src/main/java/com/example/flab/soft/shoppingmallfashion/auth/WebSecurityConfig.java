package com.example.flab.soft.shoppingmallfashion.auth;

import com.example.flab.soft.shoppingmallfashion.auth.authentication.AuthenticationFailureEntryPoint;
import com.example.flab.soft.shoppingmallfashion.auth.authentication.filter.CrewAuthenticationFilter;
import com.example.flab.soft.shoppingmallfashion.auth.authentication.filter.JwtAuthenticationFilter;
import com.example.flab.soft.shoppingmallfashion.auth.authentication.provider.CrewAuthenticationProvider;
import com.example.flab.soft.shoppingmallfashion.auth.authentication.provider.UserAuthenticationProvider;
import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetailsService.CrewAuthService;
import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetailsService.UserAuthService;
import com.example.flab.soft.shoppingmallfashion.auth.authorization.AuthorizationFailureHandler;
import com.example.flab.soft.shoppingmallfashion.auth.authorization.filter.JwtAuthorizationFilter;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.TokenProvider;
import com.example.flab.soft.shoppingmallfashion.auth.refreshToken.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserAuthService userAuthService;
    private final CrewAuthService crewAuthService;
    private final RefreshTokenService refreshTokenService;
    private final AuthorizationFailureHandler authorizationFailureHandler;
    private final AuthenticationFailureEntryPoint authenticationFailureEntryPoint;
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;
    private final ObjectPostProcessor<Object> objectPostProcessor;
    private final ApplicationContext applicationContext;
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
                .addFilterBefore(crewAuthenticationFilter(), JwtAuthenticationFilter.class)
                .addFilterBefore(jwtAuthorizationFilter(), BasicAuthenticationFilter.class)

                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers(PathRequest.toH2Console()).permitAll()
                                .requestMatchers(
                                        "/users/login",
                                        "/crews/login",
                                        "/api/v1/users/signup",
                                        "/api/v1/store/crew/signup",
                                        "/api/v1/auth/refresh-token").permitAll()
                                .requestMatchers("/api/v1/store/register").hasAuthority("ROLE_USER")
                                .requestMatchers("/api/v1/store").hasAuthority("ROLE_STORE_MANAGER")
                                .requestMatchers("/api/v1/item/management/new-item").hasAuthority("ROLE_ITEM_MANAGER")
                                .anyRequest().authenticated())

                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling
                                .accessDeniedHandler(authorizationFailureHandler)
                                .authenticationEntryPoint(authenticationFailureEntryPoint));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.authenticationManagerBuilder(objectPostProcessor, applicationContext)
                .authenticationProvider(crewAuthenticationProvider())
                .authenticationProvider(userAuthenticationProvider())
                .build();
    }

    @Bean
    public AbstractAuthenticationProcessingFilter jwtAuthenticationFilter()
            throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(objectMapper, refreshTokenService);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return jwtAuthenticationFilter;
    }

    @Bean
    public AbstractAuthenticationProcessingFilter crewAuthenticationFilter()
            throws Exception {
        CrewAuthenticationFilter crewAuthenticationFilter =
                new CrewAuthenticationFilter(objectMapper, refreshTokenService);
        crewAuthenticationFilter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return crewAuthenticationFilter;
    }

    @Bean
    public BasicAuthenticationFilter jwtAuthorizationFilter()
            throws Exception {
        return new JwtAuthorizationFilter(
                authenticationManager(authenticationConfiguration), userAuthService,
                crewAuthService, tokenProvider, objectMapper);
    }

    @Bean
    public UserAuthenticationProvider userAuthenticationProvider() {
        UserAuthenticationProvider userAuthenticationProvider = new UserAuthenticationProvider(
                passwordEncoder(), userAuthService);
        userAuthenticationProvider.setUserDetailsService(userAuthService);
        return userAuthenticationProvider;
    }

    @Bean
    public CrewAuthenticationProvider crewAuthenticationProvider() {
        CrewAuthenticationProvider crewAuthenticationProvider = new CrewAuthenticationProvider(
                passwordEncoder(), crewAuthService);
        crewAuthenticationProvider.setUserDetailsService(crewAuthService);
        return crewAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
