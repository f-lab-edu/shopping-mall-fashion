package com.example.flab.soft.shoppingmallfashion.order.domain;

import static com.example.flab.soft.shoppingmallfashion.util.NotNullValidator.*;

import com.example.flab.soft.shoppingmallfashion.common.BaseEntity;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
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
import lombok.extern.slf4j.Slf4j;

@Entity(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Slf4j
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
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_option_id")
    private ItemOption itemOption;
    private Integer orderAmount;
    private Integer totalPrice;
    private Integer discountedAmount;
    private Integer couponDiscountedAmount;
    private Integer paymentAmount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderer_id")
    private User orderer;
    @Embedded
    private DeliveryInfo deliveryInfo;
    @Embedded
    private UsedCouponInfo usedCouponInfo;

    @Builder
    public Order(Item item, ItemOption itemOption, Integer orderAmount, Integer totalPrice,
                 Integer discountedAmount, Integer couponDiscountAmount, Integer paymentAmount,
                 User orderer, DeliveryInfo deliveryInfo) {
        this.item = requireNotNull(item);
        this.itemOption = validateItemOption(itemOption);
        this.orderAmount = requireNotNull(orderAmount);
        this.totalPrice = requireNotNull(totalPrice);
        this.discountedAmount = requireNotNull(discountedAmount);
        this.couponDiscountedAmount = requireNotNull(couponDiscountAmount);
        this.paymentAmount =
                validatePaymentAmount(totalPrice, discountedAmount, couponDiscountAmount, paymentAmount);
        this.orderer = requireNotNull(orderer);
        this.deliveryInfo = requireNotNull(deliveryInfo);
    }

    private ItemOption validateItemOption(ItemOption itemOption) {
        if (this.item != requireNotNull(itemOption).getItem()) {
            throw new ApiException(ErrorEnum.INVALID_REQUEST);
        }
        return itemOption;
    }

    private Integer validatePaymentAmount(Integer totalPrice, Integer discountedAmount,
                                          Integer couponDiscountAmount, Integer paymentAmount) {
        if (paymentAmount != totalPrice - discountedAmount - couponDiscountAmount) {
            throw new ApiException(ErrorEnum.INVALID_REQUEST);
        }
        return paymentAmount;
    }

    public void setPaid() {
        if (isPaid()) {
            log.info("이중 결제 시도 주문번호: {}", id);
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
        if (isPaid()) {
            paymentStatus = PaymentStatus.ON_REFUND;
        }
    }

    private boolean isPaid() {
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

    public static Order of(OrderRequest orderRequest, Item item, ItemOption itemOption,
                           User user, DeliveryInfo deliveryInfo) {
        return builder()
                .item(item)
                .itemOption(itemOption)
                .orderAmount(orderRequest.getOrderAmount())
                .totalPrice(orderRequest.getTotalPrice())
                .discountedAmount(orderRequest.getDiscountedAmount())
                .couponDiscountAmount(orderRequest.getCouponDiscountAmount())
                .paymentAmount(orderRequest.getPaymentAmount())
                .orderer(user)
                .deliveryInfo(deliveryInfo)
                .build();
    }

    public void applyUserCoupon(UsedCouponInfo usedCouponInfo) {
        this.usedCouponInfo = usedCouponInfo;
    }
}
