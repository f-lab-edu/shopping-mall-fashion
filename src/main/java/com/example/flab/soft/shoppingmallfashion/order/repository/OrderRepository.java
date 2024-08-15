package com.example.flab.soft.shoppingmallfashion.order.repository;

import com.example.flab.soft.shoppingmallfashion.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
