package com.example.flab.soft.shoppingmallfashion.sms;

import com.example.flab.soft.shoppingmallfashion.common.RedisRepository;
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
    private final RedisRepository redisRepository;
    private static final String REDIS_VERIFY_CODE_KEY_PREFIX = "verification-code:";
    private static final String REDIS_VERIFIED_PHONE_NUMBER_PREFIX = "verified-phone-number:";
    private static final String REDIS_VERIFIED_EMAIL_PREFIX = "verified-email:";
    private static final long CODE_EXPIRED_TIME_IN_MILLISECONDS = 300 * 1000;
    private static final long VERIFIED_ID_EXPIRED_TIME_IN_MILLISECONDS = 60 * 60 * 1000;
    private static final String EMAIL_TITLE = "MallFashion 인증번호";

    public void sendMessage(String recipientPhoneNumber) {
        String code = generateVerificationCode();
        smsService.sendOne(recipientPhoneNumber, code);
        redisRepository.setData(
                REDIS_VERIFY_CODE_KEY_PREFIX + recipientPhoneNumber, code, CODE_EXPIRED_TIME_IN_MILLISECONDS);
    }

    public void sendEmail(String recipientAddress) {
        String code = generateVerificationCode();
        mailService.sendEmail(recipientAddress, EMAIL_TITLE, code);
        redisRepository.setData(
                REDIS_VERIFY_CODE_KEY_PREFIX + recipientAddress, code, CODE_EXPIRED_TIME_IN_MILLISECONDS);
    }

    private String generateVerificationCode() {
        return randomCodeGenerator.generate();
    }

    public void verifyCodeWithPhoneNumber(String phoneNumber, String code) {
        String cachedCode = redisRepository.getData(REDIS_VERIFY_CODE_KEY_PREFIX + phoneNumber);
        if (cachedCode == null || !cachedCode.equals(code)) {
            throw new ApiException(ErrorEnum.INVALID_REQUEST);
        }
        redisRepository.setData(REDIS_VERIFIED_PHONE_NUMBER_PREFIX + phoneNumber,
                phoneNumber, VERIFIED_ID_EXPIRED_TIME_IN_MILLISECONDS);
    }

    public void verifyCodeWithEamil(String email, String code) {
        String cachedCode = redisRepository.getData(REDIS_VERIFY_CODE_KEY_PREFIX + email);
        if (cachedCode == null || !cachedCode.equals(code)) {
            throw new ApiException(ErrorEnum.INVALID_REQUEST);
        }
        redisRepository.setData(REDIS_VERIFIED_EMAIL_PREFIX + email,
                email, VERIFIED_ID_EXPIRED_TIME_IN_MILLISECONDS);
    }

    public void verifyPhoneNumber(String phoneNumber) {
        if (!redisRepository.exists(REDIS_VERIFIED_PHONE_NUMBER_PREFIX + phoneNumber)) {
            throw new ApiException(ErrorEnum.INVALID_REQUEST);
        }
    }

    public void verifyEmail(String email) {
        if (!redisRepository.exists(REDIS_VERIFIED_EMAIL_PREFIX + email)) {
            throw new ApiException(ErrorEnum.INVALID_REQUEST);
        }
    }

    public Boolean isPhoneNumberAuthenticated(String phoneNumber) {
        return redisRepository.exists(REDIS_VERIFIED_PHONE_NUMBER_PREFIX + phoneNumber);
    }

    public Boolean isEmailAuthenticated(String email) {
        return redisRepository.exists(REDIS_VERIFIED_EMAIL_PREFIX + email);
    }
}
