package com.example.flab.soft.shoppingmallfashion.auth.role;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "roles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public Role(Long id, Authority authority) {
        this.id = id;
        this.authority = authority;
    }
}
