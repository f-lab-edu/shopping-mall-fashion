package com.example.flab.soft.shoppingmallfashion.order.domain;

public enum PaymentStatus {
    ON_PAYMENT, // 결제중
    PAYMENT_CANCELLED, // 결제 취소됨
    PAID, // 결제됨
    PAYMENT_FAILED, // 결제 실패
    ON_REFUND, // 환불중
    REFUND_COMPLETE, // 환불 완료
    ;
}
