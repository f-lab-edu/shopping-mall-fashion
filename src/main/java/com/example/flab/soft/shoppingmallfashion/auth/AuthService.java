package com.example.flab.soft.shoppingmallfashion.auth;

import com.example.flab.soft.shoppingmallfashion.auth.role.AdminRepository;
import com.example.flab.soft.shoppingmallfashion.auth.role.Authority;
import com.example.flab.soft.shoppingmallfashion.auth.role.AuthorityEntity;
import com.example.flab.soft.shoppingmallfashion.auth.role.RoleAuthorityEntity;
import com.example.flab.soft.shoppingmallfashion.store.repository.CrewRepository;
import com.example.flab.soft.shoppingmallfashion.store.repository.CrewRole;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;
    private final CrewRepository crewRepository;
    private final AdminRepository adminRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException((email)));

        List<Authority> authorities = new ArrayList<>();
        if (isAdmin(user)) {
            authorities.addAll(List.of(Authority.values()));
            return toAuthUser(user, authorities);
        }
        if (isCrew(user)) {
            addToAuthorities(user, authorities);
        }
        authorities.add(Authority.ROLE_USER);
        return toAuthUser(user, authorities);
    }

    private void addToAuthorities(User user, List<Authority> authorities) {
        crewRepository.findCrewWithRolesByUserId(user.getId()).ifPresent(crew ->
                crew.getCrewRoles().stream()
                        .map(CrewRole::getRoleEntity)
                        .flatMap(roleEntity -> roleEntity.getRoleAuthorities().stream())
                        .map(RoleAuthorityEntity::getAuthority)
                        .map(AuthorityEntity::getAuthority)
                        .forEach(authorities::add));
    }

    private boolean isCrew(User user) {
        return crewRepository.existsByUser(user);
    }

    private boolean isAdmin(User user) {
        return adminRepository.existsById(user.getId());
    }

    private AuthUser toAuthUser(User user, List<Authority> authorities) {
        return AuthUser.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .enabled(!user.isInactivated())
                .build();
    }
}
