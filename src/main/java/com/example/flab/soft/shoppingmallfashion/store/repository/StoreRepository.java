package com.example.flab.soft.shoppingmallfashion.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByName(String name);
}
