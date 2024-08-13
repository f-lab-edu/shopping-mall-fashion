package com.example.flab.soft.shoppingmallfashion.coupon.controller;

public enum LockType {
    DB("db"), // DB 락
    SHARED("shared"), // Redisson
    ;

    LockType(String shared) {
        this.value = shared;
    }

    private String value;
}
