package com.example.flab.soft.shoppingmallfashion.common;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OrderCountingAspect {
    private final Counter orderSuccessCounter;
    private final Counter orderFailureCounter;

    public OrderCountingAspect(MeterRegistry meterRegistry) {
        this.orderSuccessCounter = meterRegistry.counter("order.success.count");
        this.orderFailureCounter = meterRegistry.counter("order.failure.count");
    }

    @AfterReturning("execution(* com.example.flab.soft.shoppingmallfashion.order.service.OrderService.order(..))")
    public void countOrderSuccess() {
        orderSuccessCounter.increment();
    }

    @AfterThrowing("execution(* com.example.flab.soft.shoppingmallfashion.order.service.OrderService.order(..))")
    public void countOrderFailure() {
        orderFailureCounter.increment();
    }
}

