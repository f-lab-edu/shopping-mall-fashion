package com.example.flab.soft.shoppingmallfashion.util;

import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class RandomCodeGenerator {
    private final Random random = new Random();

    public String generate() {
        return String.valueOf(1000 + random.nextInt(9000));
    }
}
