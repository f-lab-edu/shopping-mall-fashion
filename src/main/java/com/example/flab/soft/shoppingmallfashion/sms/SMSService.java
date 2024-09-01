package com.example.flab.soft.shoppingmallfashion.sms;

import com.example.flab.soft.shoppingmallfashion.common.RedisUtils;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import jakarta.annotation.PostConstruct;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SMSService {
    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecretKey;
    @Value("${coolsms.api.number}")
    private String from;
    private static final String REDIS_VERIFY_CODE_KEY_PREFIX = "verification-code:";
    private static final String REDIS_VERIFIED_PHONENUMBER_KEY_PREFIX = "verified-phonenumber:";
    private static final long CODE_EXPIRED_TIME_IN_SECONDS = 30L;
    private static final long VERIFIED_PHONENUMBER_EXPIRED_TIME_IN_SECONDS = 3600L;
    private final Random random = new Random();
    private DefaultMessageService messageService;
    private final RedisUtils redisUtils;

    @PostConstruct
    private void init(){
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
    }

    public void sendOne(String to) {
        String code = generateVerificationCode();
        Message message = new Message();
        message.setFrom(from);
        message.setTo(to);
        message.setText("[MallFashion] 아래의 인증번호를 입력해주세요\n" + code);
        redisUtils.setData(REDIS_VERIFY_CODE_KEY_PREFIX + to, code, CODE_EXPIRED_TIME_IN_SECONDS);
        messageService.sendOne(new SingleMessageSendingRequest(message));
    }

    private String generateVerificationCode() {
        return String.valueOf(1000 + random.nextInt(9000));
    }

    public void cachePhoneNumberIfVerified(String phonenumber, String code) {
        String cachedCode = redisUtils.getData(REDIS_VERIFY_CODE_KEY_PREFIX + phonenumber);
        if (cachedCode == null || !cachedCode.equals(code)) {
            throw new ApiException(ErrorEnum.INVALID_REQUEST);
        }
        redisUtils.setData(REDIS_VERIFIED_PHONENUMBER_KEY_PREFIX + phonenumber,
                String.valueOf(1),
                VERIFIED_PHONENUMBER_EXPIRED_TIME_IN_SECONDS);
    }
}
