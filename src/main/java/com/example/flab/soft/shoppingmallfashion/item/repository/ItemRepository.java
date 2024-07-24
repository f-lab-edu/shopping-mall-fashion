package com.example.flab.soft.shoppingmallfashion.item.repository;

import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
import com.example.flab.soft.shoppingmallfashion.item.domain.Sex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT i FROM items i WHERE " +
            "(:minPrice IS NULL OR i.salePrice >= :minPrice) AND " +
            "(:maxPrice IS NULL OR i.salePrice <= :maxPrice) AND " +
            "(:categoryId IS NULL OR i.category.id = :categoryId) AND " +
            "(:storeId IS NULL OR i.store.id = :storeId) AND " +
            "(:sex IS NULL OR i.sex = :sex) AND " +
            "i.saleState IN ('ON_SALE', 'TEMPORARILY_SOLD_OUT')")
    Page<Item> findAllByFilters(@Param("minPrice") Integer minPrice,
                                @Param("maxPrice") Integer maxPrice,
                                @Param("categoryId") Long categoryId,
                                @Param("storeId") Long storeId,
                                @Param("sex") Sex sex,
                                Pageable pageable);
}
