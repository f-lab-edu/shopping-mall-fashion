package com.example.flab.soft.shoppingmallfashion.item.repository;

import com.example.flab.soft.shoppingmallfashion.item.domain.ItemOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ItemOptionRepository extends JpaRepository<ItemOption, Long> {
    @Modifying(clearAutomatically = true)
    @Query("UPDATE item_options io SET io.stocksCount = io.stocksCount + :stocksAmount where io.id = :id")
    void updateStocksCount(Long id, int stocksAmount);
}
