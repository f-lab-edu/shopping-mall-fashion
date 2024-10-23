package com.example.flab.soft.shoppingmallfashion.hackle;

import io.hackle.sdk.HackleClient;
import io.hackle.sdk.HackleClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HackleConfig {
    @Value("${hackle.key}")
    String sdkKey;

    @Bean
    public HackleClient hackleClient() {
        return HackleClients.create(sdkKey);
    }
}
