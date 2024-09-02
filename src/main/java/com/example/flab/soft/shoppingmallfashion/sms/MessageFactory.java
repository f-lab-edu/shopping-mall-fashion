package com.example.flab.soft.shoppingmallfashion.sms;

import net.nurigo.sdk.message.model.Message;

public class MessageFactory {
    private final String from;

    public MessageFactory(String from) {
        this.from = from;
    }

    public Message createMessage(String to, String code) {
        Message message = new Message();
        message.setFrom(from);
        message.setTo(to);
        message.setText("[MallFashion] 아래의 인증번호를 입력해주세요\n" + code);
        return message;
    }
}
