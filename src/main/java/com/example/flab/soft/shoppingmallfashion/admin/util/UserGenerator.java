package com.example.flab.soft.shoppingmallfashion.admin.util;

import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import java.util.Random;

public class UserGenerator {
    public static User generateUser(int number) {
        String email = "user" + number + "@gmail.com";
        String password = "Password1#";
        String realName = "Jane Doe";
        String nickname = "nickname" + number;
        String cellphoneNumber = generateUniquePhoneNumber(number);

        return User.builder()
                .email(email)
                .password(password)
                .realName(realName)
                .cellphoneNumber(cellphoneNumber)
                .nickname(nickname)
                .build();
    }

    private static String generateUniquePhoneNumber(int number) {
        Random random = new Random(number);
        int randomDigits = random.nextInt(90000000) + 10000000;
        return "010" + randomDigits;
    }
}

