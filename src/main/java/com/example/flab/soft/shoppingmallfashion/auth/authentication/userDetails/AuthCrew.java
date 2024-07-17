package com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetails;

import com.example.flab.soft.shoppingmallfashion.auth.jwt.Role;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@SuperBuilder
public class AuthCrew extends AuthUser implements UserDetails {
    private Long storeId;

    @Override
    public Role getRole() {
        return Role.CREW;
    }
}
