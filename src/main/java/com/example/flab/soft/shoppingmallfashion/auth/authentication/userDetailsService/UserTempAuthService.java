package com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetailsService;

import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetails.AuthUser;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserTempAuthService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user = userRepository.findByCellphoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException((phoneNumber)));

        return toAuthUser(user, List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    private AuthUser toAuthUser(User user, List<SimpleGrantedAuthority> authorities) {
        return AuthUser.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .enabled(!user.isInactivated())
                .build();
    }
}
