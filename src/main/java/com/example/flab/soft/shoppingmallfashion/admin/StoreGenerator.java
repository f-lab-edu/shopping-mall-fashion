package com.example.flab.soft.shoppingmallfashion.admin;

import com.example.flab.soft.shoppingmallfashion.store.repository.Store;
import java.util.Random;

public class StoreGenerator {
    public static Store generateStore(int number, long userId) {
        String name = "store" + number;
        String logo = "logo";
        String description = "description";
        String businessRegistrationNumber = generateUniqueBusinessRegistrationNumber(number);

        return Store.builder()
                .name(name)
                .logo(logo)
                .description(description)
                .businessRegistrationNumber(businessRegistrationNumber)
                .managerId(userId)
                .build();
    }

    private static String generateUniqueBusinessRegistrationNumber(int seed) {
        Random random = new Random(seed);
        StringBuilder businessRegNum = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            businessRegNum.append(random.nextInt(10));
        }
        return businessRegNum.toString();
    }
}

