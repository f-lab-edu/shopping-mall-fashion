package com.example.flab.soft.shoppingmallfashion.order.domain;

import static com.example.flab.soft.shoppingmallfashion.util.NotNullValidator.*;

import com.example.flab.soft.shoppingmallfashion.common.BaseEntity;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.item.domain.ItemOption;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.COMPLETED;
    @Enumerated(value = EnumType.STRING)
    private DeliveryStatus deliveryStatus = DeliveryStatus.PREPARING;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_option_id")
    private ItemOption itemOption;
    private Integer amount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderer_id")
    private User orderer;
    @Embedded
    private DeliveryInfo deliveryInfo;

    @Builder
    public Order(ItemOption itemOption, Integer amount, User orderer, DeliveryInfo deliveryInfo) {
        this.itemOption = requireNotNull(itemOption);
        this.amount = requireNotNull(amount);
        this.orderer = requireNotNull(orderer);
        this.deliveryInfo = requireNotNull(deliveryInfo);
    }

    public static Order of(ItemOption itemOption, Integer amount, User user, DeliveryInfo deliveryInfo) {
        return builder()
                .itemOption(itemOption)
                .amount(amount)
                .orderer(user)
                .deliveryInfo(deliveryInfo)
                .build();
    }

    public void cancel() {
        if (deliveryStatus != DeliveryStatus.PREPARING) {
            throw new ApiException(ErrorEnum.ALREADY_ON_DELIVERY);
        }
        orderStatus = OrderStatus.CANCELLED;
    }

    public void startDelivery() {
        if (deliveryStatus != DeliveryStatus.PREPARING) {
            throw new ApiException(ErrorEnum.ALREADY_ON_DELIVERY);
        }
        deliveryStatus = DeliveryStatus.ON_DELIVERY;
    }

    public void changeDeliveryInfo(DeliveryInfo deliveryInfo) {
        if (deliveryStatus != DeliveryStatus.PREPARING) {
            throw new ApiException(ErrorEnum.ALREADY_ON_DELIVERY);
        }
        this.deliveryInfo = deliveryInfo;
    }
}
