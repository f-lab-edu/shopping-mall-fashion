package com.example.flab.soft.shoppingmallfashion.category;

import com.example.flab.soft.shoppingmallfashion.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity(name = "medium_categories")
@Getter
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(name = "item_cnt", nullable = false)
    private Long itemCount;
    @ManyToOne
    @JoinColumn(name = "large_category_id")
    private LargeCategory largeCategory;

    public void increaseItemCount(int count) {
        itemCount += count;
    }
}
