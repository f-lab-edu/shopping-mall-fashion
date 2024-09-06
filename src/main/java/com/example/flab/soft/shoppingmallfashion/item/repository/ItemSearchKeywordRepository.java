package com.example.flab.soft.shoppingmallfashion.item.repository;

import com.example.flab.soft.shoppingmallfashion.item.domain.ItemSearchKeyword;
import com.example.flab.soft.shoppingmallfashion.item.domain.SearchKeyword;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemSearchKeywordRepository extends JpaRepository<ItemSearchKeyword, Long> {
    Optional<ItemSearchKeyword> findByItemIdAndSearchKeyword(Long itemId, SearchKeyword searchKeyword);
}
