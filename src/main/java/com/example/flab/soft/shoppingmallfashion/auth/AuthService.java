package com.example.flab.soft.shoppingmallfashion.auth;

import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException((email)));

        List<UserRole> userRoles = userRoleRepository.findAllByUserId(user.getId());
        List<Role> roles = userRoles.stream().map((UserRole::getRole)).toList();

        return AuthUser.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(roles)
                .build();
    }
}