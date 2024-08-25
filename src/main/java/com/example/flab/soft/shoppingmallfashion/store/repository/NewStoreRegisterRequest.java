package com.example.flab.soft.shoppingmallfashion.store.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "new_store_register_requests")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NewStoreRegisterRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String requesterName;
    @Column(nullable = false)
    private String requesterEmail;
    @Column(nullable = false)
    private String requesterPhoneNumber;
    @Column(nullable = false)
    private String businessRegistrationNumber;

    @Builder
    public NewStoreRegisterRequest(String requesterName, String requesterEmail, String requesterPhoneNumber,
                                   String businessRegistrationNumber) {
        this.requesterName = requesterName;
        this.requesterEmail = requesterEmail;
        this.requesterPhoneNumber = requesterPhoneNumber;
        this.businessRegistrationNumber = businessRegistrationNumber;
    }
}
