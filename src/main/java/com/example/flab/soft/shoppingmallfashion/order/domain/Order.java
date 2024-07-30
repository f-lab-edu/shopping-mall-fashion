package com.example.flab.soft.shoppingmallfashion.order.domain;

import static com.example.flab.soft.shoppingmallfashion.util.NotNullValidator.*;

import com.example.flab.soft.shoppingmallfashion.common.BaseEntity;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.item.domain.ItemOption;
import com.example.flab.soft.shoppingmallfashion.order.controller.OrderRequest;
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
    private PaymentStatus paymentStatus = PaymentStatus.ON_PAYMENT;
    @Enumerated(value = EnumType.STRING)
    private DeliveryStatus deliveryStatus = DeliveryStatus.PREPARING;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_option_id")
    private ItemOption itemOption;
    private Integer orderAmount;
    private Integer totalPrice;
    private Integer discountedAmount;
    private Integer paymentAmount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderer_id")
    private User orderer;
    @Embedded
    private DeliveryInfo deliveryInfo;

    @Builder
    public Order(ItemOption itemOption, Integer orderAmount, Integer totalPrice,
                 Integer discountedAmount, Integer paymentAmount,
                 User orderer, DeliveryInfo deliveryInfo) {
        this.itemOption = requireNotNull(itemOption);
        this.orderAmount = requireNotNull(orderAmount);
        this.totalPrice = requireNotNull(totalPrice);
        this.discountedAmount = requireNotNull(discountedAmount);
        this.paymentAmount = validatePaymentAmount(totalPrice, discountedAmount, paymentAmount);
        this.orderer = requireNotNull(orderer);
        this.deliveryInfo = requireNotNull(deliveryInfo);
    }

    private Integer validatePaymentAmount(Integer totalPrice,
                                    Integer discountedAmount, Integer paymentAmount) {
        if (paymentAmount == totalPrice - discountedAmount) {
            return paymentAmount;
        } else throw new ApiException(ErrorEnum.INVALID_REQUEST);
    }

    public void setPaid() {
        if (isPaymentSucceed()) {
            throw new ApiException(ErrorEnum.ALREADY_PAID);
        }
        paymentStatus = PaymentStatus.PAID;
        orderStatus = OrderStatus.COMPLETED;
    }

    public void cancel() {
        if (deliveryStatus != DeliveryStatus.PREPARING) {
            throw new ApiException(ErrorEnum.ALREADY_ON_DELIVERY);
        }
        orderStatus = OrderStatus.CANCELLED;
        if (isPaymentNotSucceed()) {
            paymentStatus = PaymentStatus.PAYMENT_CANCELLED;
            return;
        }
        if (isPaymentSucceed()) {
            paymentStatus = PaymentStatus.ON_REFUND;
        }
    }

    private boolean isPaymentSucceed() {
        return paymentStatus == PaymentStatus.PAID;
    }

    public void startDelivery() {
        if (deliveryStatus != DeliveryStatus.PREPARING) {
            throw new ApiException(ErrorEnum.ALREADY_ON_DELIVERY);
        }
        if (isPaymentNotSucceed()){
            throw new ApiException(ErrorEnum.NEED_PAYMENT);
        }
        deliveryStatus = DeliveryStatus.ON_DELIVERY;
    }

    private boolean isPaymentNotSucceed() {
        return paymentStatus == PaymentStatus.ON_PAYMENT || paymentStatus == PaymentStatus.PAYMENT_FAILED;
    }

    public void changeDeliveryInfo(DeliveryInfo deliveryInfo) {
        if (deliveryStatus != DeliveryStatus.PREPARING) {
            throw new ApiException(ErrorEnum.ALREADY_ON_DELIVERY);
        }
        this.deliveryInfo = deliveryInfo;
    }

    public static Order of(OrderRequest orderRequest, ItemOption itemOption, User user, DeliveryInfo deliveryInfo) {
        return builder()
                .itemOption(itemOption)
                .orderAmount(orderRequest.getOrderAmount())
                .totalPrice(orderRequest.getTotalPrice())
                .discountedAmount(orderRequest.getDiscountedAmount())
                .paymentAmount(orderRequest.getPaymentAmount())
                .orderer(user)
                .deliveryInfo(deliveryInfo)
                .build();
    }
}
