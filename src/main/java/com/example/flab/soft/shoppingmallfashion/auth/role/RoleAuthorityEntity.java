package com.example.flab.soft.shoppingmallfashion.auth.role;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "role_authorities")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RoleAuthorityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;
    @ManyToOne
    @JoinColumn(name = "authority_id")
    private AuthorityEntity authority;
}
