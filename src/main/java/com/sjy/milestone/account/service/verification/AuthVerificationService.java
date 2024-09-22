package com.sjy.milestone.account.service.verification;

import com.sjy.milestone.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthVerificationService {

    private final RedisService redisService;

    public boolean verifyEmailCode(String email, String verificationCode) {
        String storedCode = redisService.getData(email + ":code");
        if (!storedCode.equals(verificationCode)) {
            redisService.deleteData(email + ":verification");
            return false;
        }

        redisService.setData(email + ":verified", "true", 0);
        redisService.deleteData(email + ":code");
        return true;
    }

    public boolean verifyPhoneCode(String phoneNum, String verificationCode) {
        String storedCode = redisService.getData(phoneNum + ":code");
        if (!storedCode.equals(verificationCode)) {
            redisService.deleteData(phoneNum + ":verification");
            return false;
        }

        redisService.setData(phoneNum + ":verified", "true", 0);
        redisService.deleteData(phoneNum + ":verification");
        return true;
    }
}