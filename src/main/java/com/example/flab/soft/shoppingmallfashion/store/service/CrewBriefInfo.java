package com.example.flab.soft.shoppingmallfashion.store.service;

import com.example.flab.soft.shoppingmallfashion.store.repository.Crew;
import java.time.LocalDateTime;
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

    @Builder
    public CrewBriefInfo(Crew crew) {
        this.id = crew.getId();
        this.name = crew.getName();
        this.createdAt = crew.getCreatedAt();
        this.approved = crew.getApproved();
    }
}
