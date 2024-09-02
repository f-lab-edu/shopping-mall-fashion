package com.example.flab.soft.shoppingmallfashion.sms;

import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Service;

@Service
public class CoolSmsService implements SmsService{
    private MessageFactory messageFactory;
    private DefaultMessageService messageService;

    @Override
    public void sendOne(String to, String code) {
        messageService.sendOne(
                new SingleMessageSendingRequest(messageFactory.createMessage(to, code)));
    }
}
