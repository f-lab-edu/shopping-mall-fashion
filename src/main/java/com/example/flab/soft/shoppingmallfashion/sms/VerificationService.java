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
    private final MailService mailService;
    private final RandomCodeGenerator randomCodeGenerator;
    private final RedisUtils redisUtils;
    private static final String REDIS_VERIFY_CODE_KEY_PREFIX = "verification-code:";
    private static final String REDIS_VERIFIED_ID_PREFIX = "verified-id:";
    private static final long CODE_EXPIRED_TIME_IN_MILLISECONDS = 300 * 1000;
    private static final long VERIFIED_PHONENUMBER_EXPIRED_TIME_IN_MILLISECONDS = 60 * 60 * 1000;
    private static final String EMAIL_TITLE = "MallFashion 인증번호";

    public void sendMessage(String to) {
        String code = generateVerificationCode();
        smsService.sendOne(to, code);
        cache(to, code);
    }

    public void sendEMail(String to) {
        String code = generateVerificationCode();
        mailService.sendEmail(to, EMAIL_TITLE, code);
        cache(to, code);
    }

    private void cache(String to, String code) {
        redisUtils.setData(REDIS_VERIFY_CODE_KEY_PREFIX + to, code, CODE_EXPIRED_TIME_IN_MILLISECONDS);
    }

    private String generateVerificationCode() {
        return randomCodeGenerator.generate();
    }

    public void cacheEmailOrPhoneNumberIfVerified(String emailOrPhoneNumber, String code) {
        String cachedCode = redisUtils.getData(REDIS_VERIFY_CODE_KEY_PREFIX + emailOrPhoneNumber);
        if (cachedCode == null || !cachedCode.equals(code)) {
            throw new ApiException(ErrorEnum.INVALID_REQUEST);
        }
        redisUtils.setData(REDIS_VERIFIED_ID_PREFIX + emailOrPhoneNumber,
                String.valueOf(1),
                VERIFIED_PHONENUMBER_EXPIRED_TIME_IN_MILLISECONDS);
    }
}
