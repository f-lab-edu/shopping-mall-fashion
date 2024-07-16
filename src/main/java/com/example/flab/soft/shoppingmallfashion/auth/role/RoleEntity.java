package com.example.flab.soft.shoppingmallfashion.auth.role;

import com.example.flab.soft.shoppingmallfashion.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "roles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RoleEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    private List<RoleAuthorityEntity> roleAuthorities = new ArrayList<>();

    @Builder
    public RoleEntity(Role role) {
        this.role = role;
    }
}
