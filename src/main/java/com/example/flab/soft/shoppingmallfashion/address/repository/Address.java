package com.example.flab.soft.shoppingmallfashion.address.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity(name = "addresses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    public Address(String recipientName, String roadAddress, String addressDetail,
                   int zipcode, String recipientCellphone, Long userId) {
        this.recipientName = recipientName;
        this.roadAddress = roadAddress;
        this.addressDetail = addressDetail;
        this.zipcode = zipcode;
        this.recipientCellphone = recipientCellphone;
        this.userId = userId;
    }
}
