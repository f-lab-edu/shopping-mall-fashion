package com.example.flab.soft.shoppingmallfashion.category;

import javax.net.ssl.SSLSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findFirstBy();
}
