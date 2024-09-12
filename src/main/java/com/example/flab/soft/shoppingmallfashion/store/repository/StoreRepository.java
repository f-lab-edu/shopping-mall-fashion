package com.example.flab.soft.shoppingmallfashion.store.repository;

import java.util.Optional;
import javax.net.ssl.SSLSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByName(String name);
    Optional<Store> findByManagerId(Long id);
    Store findFirstBy();
}
