package com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetails;

import com.example.flab.soft.shoppingmallfashion.auth.jwt.Role;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@SuperBuilder
public class AuthUser implements UserDetails {
    private Long id;
    private String email;
    private String password;
    private List<SimpleGrantedAuthority> authorities;
    private boolean enabled;

    public Role getRole() {
        return Role.USER;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
