package com.example.flab.soft.shoppingmallfashion.store.repository;

import com.example.flab.soft.shoppingmallfashion.auth.role.RoleEntity;
import com.example.flab.soft.shoppingmallfashion.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "crew_roles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(exclude = {"id"}, callSuper = false)
public class CrewRole extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "crew_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Crew crew;
    @JoinColumn(name = "role_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RoleEntity roleEntity;

    @Builder
    public CrewRole(Crew crew, RoleEntity roleEntity) {
        this.crew = crew;
        this.roleEntity = roleEntity;
    }
}
