package com.example.flab.soft.shoppingmallfashion.item.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "item_search_keywords")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemSearchKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long itemId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "search_keyword_id")
    private SearchKeyword searchKeyword;

    @Builder
    public ItemSearchKeyword(Long itemId, SearchKeyword searchKeyword) {
        this.itemId = itemId;
        this.searchKeyword = searchKeyword;
    }
}
