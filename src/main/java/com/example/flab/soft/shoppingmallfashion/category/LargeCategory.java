package com.example.flab.soft.shoppingmallfashion.category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "large_categories")
public class LargeCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(name = "sub_category_cnt", nullable = false)
    private Integer subCategoryCount;
    @Column(name = "item_cnt",nullable = false)
    private Long itemCount;
}
