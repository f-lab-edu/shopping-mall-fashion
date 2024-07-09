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
    Long id = 0L;
    String name;
    String logo;
    String description;
    String businessRegistrationNumber;
    Long managerId;

    @Builder
    public Store(String name, String logo, String description, String businessRegistrationNumber, Long managerId) {
        this.name = name;
        this.logo = logo;
        this.description = description;
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.managerId = managerId;
    }
}
