package com.example.flab.soft.shoppingmallfashion.item.repository;

import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
