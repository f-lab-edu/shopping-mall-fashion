package com.example.flab.soft.shoppingmallfashion.item.domain;

import com.example.flab.soft.shoppingmallfashion.common.BaseEntity;
import jakarta.persistence.Column;
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

@Entity(name = "item_relations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemRelation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long itemId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_item_id")
    private Item relatedItem;
    private Long weight = 1L;

    @Builder
    public ItemRelation(Long itemId, Item relatedItem) {
        this.itemId = itemId;
        this.relatedItem = relatedItem;
    }

    public void increaseWeight() {
        weight++;
    }
}
