package com.example.flab.soft.shoppingmallfashion.store.repository;

import com.example.flab.soft.shoppingmallfashion.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    private String email;
    private String password;
    private String name;
    private String cellphoneNumber;
    private Boolean approved = false;
    private Boolean withdrawal = false;
    @JoinColumn(name = "store_id")
    @ManyToOne
    private Store store;
    @OneToMany(mappedBy = "crew")
    private List<CrewRole> crewRoles = new ArrayList<>();

    @Builder
    public Crew(String email, String password, String name, String cellphoneNumber, Store store) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.cellphoneNumber = cellphoneNumber;
        this.store = store;
    }

    public void addRole(CrewRole crewRole) {
        if (!crewRoles.contains(crewRole)) {
            crewRoles.add(crewRole);
        }
    }

    public void withdraw() {
        withdrawal = true;
    }

    public boolean isInactivated(){
        return withdrawal;
    }

    public void beApproved() {
        approved = true;
    }

    public boolean isApproved() {
        return approved;
    }
}
