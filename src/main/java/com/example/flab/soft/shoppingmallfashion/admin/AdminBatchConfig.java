package com.example.flab.soft.shoppingmallfashion.admin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@RequiredArgsConstructor
public class AdminBatchConfig {
    private final PlatformTransactionManager txManager;

    @Bean
    public TransactionTemplate transactionTemplate() {
        return new TransactionTemplate(txManager);
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(16);
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setMaxPoolSize(32);
        return threadPoolTaskExecutor;
    }
}
