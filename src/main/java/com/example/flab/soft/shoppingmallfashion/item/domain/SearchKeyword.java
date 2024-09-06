package com.example.flab.soft.shoppingmallfashion.item.domain;

import com.example.flab.soft.shoppingmallfashion.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "search_keywords")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SearchKeyword extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Builder
    public SearchKeyword(String name) {
        this.name = name;
    }
}
