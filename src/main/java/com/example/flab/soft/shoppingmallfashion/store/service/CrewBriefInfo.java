package com.example.flab.soft.shoppingmallfashion.store.service;

import com.example.flab.soft.shoppingmallfashion.auth.role.Role;
import com.example.flab.soft.shoppingmallfashion.store.repository.Crew;
import com.example.flab.soft.shoppingmallfashion.store.repository.CrewRole;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CrewBriefInfo {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private boolean approved;
    private List<Role> roles;

    @Builder
    public CrewBriefInfo(Crew crew, List<Role> roles) {
        this.id = crew.getId();
        this.name = crew.getName();
        this.createdAt = crew.getCreatedAt();
        this.approved = crew.getApproved();
        this.roles = roles;
    }
}
