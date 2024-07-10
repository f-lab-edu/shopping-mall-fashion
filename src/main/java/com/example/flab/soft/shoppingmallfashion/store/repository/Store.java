package com.example.flab.soft.shoppingmallfashion.store.repository;

import com.example.flab.soft.shoppingmallfashion.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "stores")
@Getter
@NoArgsConstructor
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
    private String name;
    private String logo;
    private String description;
    private String businessRegistrationNumber;
    private Long managerId;

    @Builder
    public Store(String name, String logo, String description, String businessRegistrationNumber, Long managerId) {
        this.name = name;
        this.logo = logo;
        this.description = description;
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.managerId = managerId;
    }

    public void update(String type, String value) {
        if (Objects.equals(type, "name")) {
            name = value;
        }
        else if (Objects.equals(type, "logo")) {
            logo = value;
        }
        else if (Objects.equals(type, "description")) {
            logo = description;
        }
    }
}
