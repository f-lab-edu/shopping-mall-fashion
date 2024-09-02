package com.example.flab.soft.shoppingmallfashion.sms;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.flab.soft.shoppingmallfashion.common.RedisUtils;
import com.example.flab.soft.shoppingmallfashion.util.RandomCodeGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VerificationServiceTest {
    @Mock
    private SmsService smsService;
    @Mock
    private RandomCodeGenerator randomCodeGenerator;
    @Mock
    private RedisUtils redisUtils;
    @InjectMocks
    private VerificationService verificationService;

    private static final String PHONE_NUMBER = "01012345678";
    private static final String VERIFICATION_CODE = "1234";

    @Test
    @DisplayName("인증코드 전송 후 저장")
    void whenafterSendingCode_saveVerificationCode() throws Exception {
        //given
        doNothing().when(smsService).sendOne(PHONE_NUMBER, VERIFICATION_CODE);
        when(randomCodeGenerator.generate()).thenReturn(VERIFICATION_CODE);
        //when
        verificationService.sendOne(PHONE_NUMBER);
        // then
        verify(smsService).sendOne(PHONE_NUMBER, VERIFICATION_CODE);
        verify(redisUtils).setData(any(), any(), any());
    }
}
