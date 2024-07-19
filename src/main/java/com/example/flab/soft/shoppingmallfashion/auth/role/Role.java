package com.example.flab.soft.shoppingmallfashion.auth.role;

import java.util.List;
import lombok.Getter;

@Getter
public enum Role {
    USER(Authority.ROLE_USER),
    ITEM_MANAGER(Authority.ROLE_USER, Authority.ITEM_MANAGEMENT),
    STOCK_MANAGER(Authority.ROLE_USER, Authority.STOCK_MANAGEMENT),
    CREW_MANGER(Authority.ROLE_USER, Authority.CREW_MANAGEMENT),
    STORE_MANAGER_BEFORE_APPROVAL(Authority.ROLE_USER, Authority.STORE_MANAGEMENT),
    STORE_MANAGER(Authority.ROLE_USER, Authority.STORE_MANAGEMENT, Authority.CREW_MANAGEMENT, Authority.ITEM_MANAGEMENT, Authority.STOCK_MANAGEMENT),
    ADMIN(Authority.ROLE_USER, Authority.ROLE_ADMIN, Authority.STORE_APPROVAL, Authority.STORE_MANAGEMENT,
            Authority.CREW_MANAGEMENT, Authority.ITEM_MANAGEMENT, Authority.STOCK_MANAGEMENT);

    private final List<Authority> authorities;

    Role(Authority... authorities) {
        this.authorities = List.of(authorities);
    }
}
