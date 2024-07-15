package com.example.flab.soft.shoppingmallfashion.item.repository;

import com.example.flab.soft.shoppingmallfashion.item.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
