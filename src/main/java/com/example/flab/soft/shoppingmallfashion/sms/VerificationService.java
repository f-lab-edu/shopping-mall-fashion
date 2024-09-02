package com.example.flab.soft.shoppingmallfashion.sms;

import com.example.flab.soft.shoppingmallfashion.common.RedisUtils;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.util.RandomCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationService {
    private final SmsService smsService;
    private final RandomCodeGenerator randomCodeGenerator;
    private final RedisUtils redisUtils;
    private static final String REDIS_VERIFY_CODE_KEY_PREFIX = "verification-code:";
    private static final String REDIS_VERIFIED_PHONENUMBER_KEY_PREFIX = "verified-phonenumber:";
    private static final long CODE_EXPIRED_TIME_IN_MILLISECONDS = 30 * 1000;
    private static final long VERIFIED_PHONENUMBER_EXPIRED_TIME_IN_MILLISECONDS = 60 * 60 * 1000;


    public void sendOne(String to) {
        String code = generateVerificationCode();
        smsService.sendOne(to, code);
        redisUtils.setData(REDIS_VERIFY_CODE_KEY_PREFIX + to, code, CODE_EXPIRED_TIME_IN_MILLISECONDS);
    }

    private String generateVerificationCode() {
        return randomCodeGenerator.generate();
    }

    public void cachePhoneNumberIfVerified(String phonenumber, String code) {
        String cachedCode = redisUtils.getData(REDIS_VERIFY_CODE_KEY_PREFIX + phonenumber);
        if (cachedCode == null || !cachedCode.equals(code)) {
            throw new ApiException(ErrorEnum.INVALID_REQUEST);
        }
        redisUtils.setData(REDIS_VERIFIED_PHONENUMBER_KEY_PREFIX + phonenumber,
                String.valueOf(1),
                VERIFIED_PHONENUMBER_EXPIRED_TIME_IN_MILLISECONDS);
    }
}
