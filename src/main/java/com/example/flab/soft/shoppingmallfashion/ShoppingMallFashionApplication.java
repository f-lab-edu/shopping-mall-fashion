package com.example.flab.soft.shoppingmallfashion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ShoppingMallFashionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingMallFashionApplication.class, args);
	}

}
