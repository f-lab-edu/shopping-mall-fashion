package com.example.flab.soft.shoppingmallfashion.category;

import com.example.flab.soft.shoppingmallfashion.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(name = "item_count", nullable = false)
    private Long itemCount = 0L;
    @ManyToOne
    @JoinColumn(name = "large_category_id")
    private LargeCategory largeCategory;

    @Builder
    public Category(String name, LargeCategory largeCategory) {
        this.name = name;
        this.largeCategory = largeCategory;
    }

    public void increaseItemCount(int count) {
        itemCount += count;
    }
}
