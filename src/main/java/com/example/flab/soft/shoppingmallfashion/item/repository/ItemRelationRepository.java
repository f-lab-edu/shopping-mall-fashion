package com.example.flab.soft.shoppingmallfashion.item.repository;

import com.example.flab.soft.shoppingmallfashion.item.domain.ItemRelation;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemRelationRepository extends JpaRepository<ItemRelation, Long> {
    @Query("SELECT ir FROM item_relations ir JOIN FETCH ir.relatedItem "
            + "WHERE ir.itemId = :itemId ORDER BY ir.weight DESC")
    List<ItemRelation> findTopRelatedById(Long itemId, Pageable pageable);
}
