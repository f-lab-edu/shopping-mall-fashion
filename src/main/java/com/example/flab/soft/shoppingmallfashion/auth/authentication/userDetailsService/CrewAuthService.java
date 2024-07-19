package com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetailsService;

import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetails.AuthCrew;
import com.example.flab.soft.shoppingmallfashion.auth.role.Authority;
import com.example.flab.soft.shoppingmallfashion.auth.role.AuthorityEntity;
import com.example.flab.soft.shoppingmallfashion.auth.role.AuthorityRepository;
import com.example.flab.soft.shoppingmallfashion.store.repository.Crew;
import com.example.flab.soft.shoppingmallfashion.store.repository.CrewRepository;
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
public class CrewAuthService implements UserDetailsService {
    private final CrewRepository crewRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Crew crew = crewRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException((username)));
        List<AuthorityEntity> authorityEntities = authorityRepository.findAllByCrewId(crew.getId());
        return toAuthCrew(crew, authorityEntities);
    }

    private AuthCrew toAuthCrew(Crew crew, List<AuthorityEntity> authorityEntities) {
        List<SimpleGrantedAuthority> authorities = toGrantedAuthorities(authorityEntities);
        return AuthCrew.builder()
                .id(crew.getId())
                .email(crew.getEmail())
                .password(crew.getPassword())
                .storeId(crew.getStore().getId())
                .authorities(authorities)
                .enabled(!crew.isInactivated())
                .build();
    }

    private List<SimpleGrantedAuthority> toGrantedAuthorities(List<AuthorityEntity> authorityEntities) {
        return authorityEntities.stream()
                .map(AuthorityEntity::getAuthority)
                .map(Authority::name)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
