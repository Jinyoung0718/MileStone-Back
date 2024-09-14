package com.sjy.milestone.Util;

import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmsUtil {

    @Value("${cool_sms.api.key}")
    private String apiKey;
    @Value("${cool_sms.api.secret}")
    private String apiSecretKey;

    private DefaultMessageService messageService;

    @PostConstruct
    private void init(){
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
    }

    public void sendCode(String phoneNum, String verificationCode) {
        Message message = new Message();
        message.setFrom("01058860982");
        message.setTo(phoneNum);
        message.setText("[MileStone] 아래의 인증번호를 입력해주세요\n" + verificationCode);

        this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }

}
