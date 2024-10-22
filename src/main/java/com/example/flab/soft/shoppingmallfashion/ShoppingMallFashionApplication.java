package com.example.flab.soft.shoppingmallfashion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync
@EnableCaching
@EnableTransactionManagement
public class ShoppingMallFashionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingMallFashionApplication.class, args);
	}

}
