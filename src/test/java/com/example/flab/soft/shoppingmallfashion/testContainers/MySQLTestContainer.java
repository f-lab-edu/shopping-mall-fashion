package com.example.flab.soft.shoppingmallfashion.testContainers;

import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.MySQLContainer;

@Configuration
public class MySQLTestContainer {
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withInitScript("schema.sql")
            .withDatabaseName("test")
            .withUsername("root")
            .withPassword("rootuser");

    static {
        mysqlContainer.start();
        System.setProperty("spring.datasource.url", mysqlContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", mysqlContainer.getUsername());
        System.setProperty("spring.datasource.password", mysqlContainer.getPassword());
    }
}
