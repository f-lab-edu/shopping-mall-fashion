package com.example.flab.soft.shoppingmallfashion.item.repository;

import com.example.flab.soft.shoppingmallfashion.item.domain.ItemOption;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ItemOptionRepository extends JpaRepository<ItemOption, Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE item_options io SET io.stocksCount = io.stocksCount + :stocksAmount where io.id = :id")
    void updateStocksCount(Long id, int stocksAmount);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT io FROM item_options io JOIN FETCH io.item WHERE io.id = :id AND io.stocksCount >= :stocksAmount")
    Optional<ItemOption> findByIdForUpdate(Long id, int stocksAmount);
}
