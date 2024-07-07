package com.example.flab.soft.shoppingmallfashion.address.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity(name = "addresses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id = 0L;
    private String recipientName;
    private String roadAddress;
    private String addressDetail;
    private int zipcode;
    private String recipientCellphone;
    private Long userId;

    @Builder
    public Address(String recipientName, String roadAddress, String addressDetail, int zipcode,
                   String recipientCellphone,
                   Long userId) {
        this.recipientName = recipientName;
        this.roadAddress = roadAddress;
        this.addressDetail = addressDetail;
        this.zipcode = zipcode;
        this.recipientCellphone = recipientCellphone;
        this.userId = userId;
    }
}
