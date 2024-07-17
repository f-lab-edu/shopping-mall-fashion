package com.example.flab.soft.shoppingmallfashion.store.repository;

import com.example.flab.soft.shoppingmallfashion.common.BaseEntity;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "crews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Crew extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "user_id")
    @OneToOne
    private User user;
    @JoinColumn(name = "store_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;
    @OneToMany(mappedBy = "crew")
    private List<CrewRole> crewRoles = new ArrayList<>();

    @Builder
    public Crew(User user, Store store) {
        this.user = user;
        this.store = store;
    }

    public void addRole(CrewRole crewRole) {
        if (!crewRoles.contains(crewRole)) {
            crewRoles.add(crewRole);
        }
    }
}
