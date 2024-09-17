package com.sjy.milestone.account.service;

import com.sjy.milestone.account.repository.MemberRepository;
import com.sjy.milestone.exception.badrequest.BadRequestTelException;
import com.sjy.milestone.memberaddress.repository.MemberAddressRepository;
import com.sjy.milestone.redis.RedisService;
import com.sjy.milestone.util.SmsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MemberAddressRepository memberAddressRepository;
    private final MemberRepository memberRepository;
    private final RedisService redisService;
    private final JavaMailSender mailSender;
    private final SmsUtil smsUtil;

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    public void emailSendCode(String toEmail, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("이메일 인증 코드");
        message.setText("인증 코드는 " + verificationCode + " 입니다.");
        mailSender.send(message);
    }

    public void sendEmailVerificationCode(String email) {

        if (memberRepository.existsByUserEmail(email)) {
            throw new BadRequestTelException("이미 등록된 이메일 입니다.");
        }

        if (redisService.getData(email + ":verified").equals("true")) {
            throw new IllegalStateException("이메일 인증이 이미 완료되었습니다.");
        }

        if (redisService.setIfAbsent(email + ":verification", "pending", 30)) {
            throw new IllegalStateException("이메일 인증 코드가 이미 발송 대기 중입니다 30초 뒤에 재요청 보내십시오.");
        }

        String verificationCode = generateVerificationCode();
        emailSendCode(email, verificationCode);
        redisService.setData(email + ":code", verificationCode, 600);
    }

    public void sendPhoneVerificationCode(String phoneNum) {

        if (memberAddressRepository.existsByTel(phoneNum)) {
            throw new BadRequestTelException("이미 등록된 휴대전화 번호입니다.");
        }

        if (redisService.getData(phoneNum + ":verified").equals("true")) {
            throw new IllegalStateException("휴대전화 인증이 이미 완료되었습니다.");
        }

        if (redisService.setIfAbsent(phoneNum + ":verification", "pending", 30)) {
            throw new IllegalStateException("해당 번호로 이미 인증코드를 발송하였습니다 30초 뒤에 재요청 보내십시오.");
        }

        String verificationCode = generateVerificationCode();
        smsUtil.sendCode(phoneNum, verificationCode);
        redisService.setData(phoneNum + ":code", verificationCode, 600); // 인증번호 10분 유효
    }
}