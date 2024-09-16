package com.example.flab.soft.shoppingmallfashion.item.repository;

import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
import com.example.flab.soft.shoppingmallfashion.item.domain.Sex;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT i FROM items i JOIN FETCH i.store s " +
            "JOIN FETCH i.category c JOIN FETCH c.largeCategory lc "
            + "WHERE " +
            "(:storeId IS NULL OR i.store.id = :storeId) AND " +
            "(:categoryId IS NULL OR i.category.id = :categoryId) AND " +
            "(:minPrice IS NULL OR i.salePrice >= :minPrice) AND " +
            "(:maxPrice IS NULL OR i.salePrice <= :maxPrice) AND " +
            "(:sex IS NULL OR i.sex = :sex) AND " +
            "i.saleState IN ('ON_SALE', 'TEMPORARILY_SOLD_OUT')")
    List<Item> findAllByFilters(@Param("minPrice") Integer minPrice,
                                @Param("maxPrice") Integer maxPrice,
                                @Param("categoryId") Long categoryId,
                                @Param("storeId") Long storeId,
                                @Param("sex") Sex sex,
                                Pageable pageable);

    @Query("SELECT COUNT (i) FROM items i WHERE " +
            "(:minPrice IS NULL OR i.salePrice >= :minPrice) AND " +
            "(:maxPrice IS NULL OR i.salePrice <= :maxPrice) AND " +
            "(:categoryId IS NULL OR i.category.id = :categoryId) AND " +
            "(:storeId IS NULL OR i.store.id = :storeId) AND " +
            "(:sex IS NULL OR i.sex = :sex) AND " +
            "i.saleState IN ('ON_SALE', 'TEMPORARILY_SOLD_OUT')")
    Long countByFilters(@Param("minPrice") Integer minPrice,
                                @Param("maxPrice") Integer maxPrice,
                                @Param("categoryId") Long categoryId,
                                @Param("storeId") Long storeId,
                                @Param("sex") Sex sex);

    @Query("SELECT i FROM items i JOIN FETCH i.store s "
            + "JOIN FETCH i.category c JOIN FETCH c.largeCategory lc "
            + "JOIN FETCH i.itemOptions io "
            + "WHERE i.id = :id")
    Optional<Item> findItemJoinFetchById(Long id);

    @Query("SELECT i FROM items i WHERE i.category.id = :categoryId "
            + "ORDER BY i.itemStats.orderCount DESC")
    List<Item> findTopItemsByCategoryId(Long categoryId, Pageable pageable);

    @Query("SELECT i FROM items i WHERE i.store.id = :storeId "
            + "ORDER BY i.itemStats.orderCount DESC")
    List<Item> findTopItemsByStoreId(Long storeId, Pageable pageable);

    @Query("SELECT i FROM items i JOIN FETCH i.store s " +
            "JOIN FETCH i.category c JOIN FETCH c.largeCategory lc "
            + "JOIN i.itemSearchKeywords isk "
            + "WHERE isk.searchKeyword = :keyword")
    Page<Item> findAllWithKeyword(String keyword, Pageable pageable);
}
