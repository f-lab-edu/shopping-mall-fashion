package com.example.flab.soft.shoppingmallfashion.store.repository;

import static com.example.flab.soft.shoppingmallfashion.util.NotNullValidator.*;

import com.example.flab.soft.shoppingmallfashion.common.BaseEntity;
import com.example.flab.soft.shoppingmallfashion.store.service.StoreUpdateDto;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Column(nullable = false)
    private String name;
    private String logo;
    private String description;
    @Column(nullable = false)
    private String businessRegistrationNumber;
    @Column(nullable = false)
    private Long managerId;
    @Enumerated(EnumType.STRING)
    private StoreState saleState = StoreState.PREPARING;

    @Builder
    public Store(String name, String logo, String description, String businessRegistrationNumber, Long managerId) {
        this.name = requireNotNull(name);
        this.logo = logo;
        this.description = description;
        this.businessRegistrationNumber = requireNotNull(businessRegistrationNumber);
        this.managerId = requireNotNull(managerId);
    }

    public void update(StoreUpdateDto storeUpdateDto) {
        if (StringUtils.isNotBlank(storeUpdateDto.getName())) {
            this. name = storeUpdateDto.getName();
        }
        if (StringUtils.isNotBlank(storeUpdateDto.getLogo())) {
            this. name = storeUpdateDto.getLogo();
        }
        if (StringUtils.isNotBlank(storeUpdateDto.getDescription())) {
            this. name = storeUpdateDto.getDescription();
        }
    }

    public void beOnStoppage() {
        saleState = StoreState.ON_STOPPAGE;
    }
}
