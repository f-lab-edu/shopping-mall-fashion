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

@Entity(name = "item_search_tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemSearchTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long itemId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "search_tag_id")
    private SearchTag searchTag;

    @Builder
    public ItemSearchTag(Long itemId, SearchTag searchTag) {
        this.itemId = itemId;
        this.searchTag = searchTag;
    }
}
