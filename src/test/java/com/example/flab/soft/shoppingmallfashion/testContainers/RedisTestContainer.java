package com.example.flab.soft.shoppingmallfashion.testContainers;

import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
public class RedisTestContainer {
    private static final String REDIS_IMAGE = "redis:7.0.8-alpine";
    private static final int REDIS_PORT = 6379;

    static {
        GenericContainer<?> REDIS_CONTAINER =
                new GenericContainer<>(DockerImageName.parse(REDIS_IMAGE))
                        .withExposedPorts(REDIS_PORT)
                        .withReuse(true);

        REDIS_CONTAINER.start();

        System.setProperty("spring.data.redis.host", REDIS_CONTAINER.getHost());
        System.setProperty("spring.data.redis.port", REDIS_CONTAINER.getMappedPort(6379).toString());
    }
}
