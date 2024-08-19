package com.example.flab.soft.shoppingmallfashion.category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "large_categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LargeCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(name = "sub_category_cnt", nullable = false)
    private Integer subCategoryCount = 0;
    @Column(name = "item_cnt",nullable = false)
    private Long itemCount = 0L;

    @Builder
    public LargeCategory(String name) {
        this.name = name;
    }
}
