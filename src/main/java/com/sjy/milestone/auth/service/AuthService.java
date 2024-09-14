package com.sjy.milestone.auth.service;

import com.sjy.milestone.Exception.BadRequestEmailException;
import com.sjy.milestone.auth.repository.AdminPhoneNumberRepository;
import com.sjy.milestone.auth.repository.MemberRepository;
import com.sjy.milestone.Redis.RedisService;
import com.sjy.milestone.Util.SmsUtil;
import com.sjy.milestone.auth.entity.Member;
import com.sjy.milestone.auth.dto.EmailDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service @Slf4j @RequiredArgsConstructor @Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final SmsUtil smsUtil;
    private final RedisService redisService;
    private final Set<String> pendingEmail = ConcurrentHashMap.newKeySet();
    private final AdminPhoneNumberRepository adminPhoneNumberRepository;

    public void sendVerificationCode(String phoneNum) {
        String verificationCode = generateVerificationCode();
        smsUtil.sendCode(phoneNum, verificationCode);
        redisService.setData(phoneNum, verificationCode,60*10L);
        log.info("휴대폰 {}으로 인증 코드 {} 발송", phoneNum, verificationCode);
    }

//    빠른 데이터 접근: Redis 는 메모리 기반의 데이터 저장소로, 인증 코드를 빠르게 저장하고 조회할 수 있습니다.
//    TTL 기능: Redis 의 TTL(Time-To-Live) 설정을 통해 인증 코드의 유효 기간을 관리하고, 자동으로 삭제되도록 합니다.
//    임시 데이터 저장: 인증 코드와 같은 일시적인 데이터를 효율적으로 저장하고 관리하기 위해 Redis 를 캐시처럼 활용합니다.

    public boolean verifyCode(String phoneNum, String verificationCode) {
        String storedCode = redisService.getData(phoneNum);
        if (storedCode == null || !storedCode.equals(verificationCode)) {
            return false;
        }
        redisService.deleteData(phoneNum);
        return true;
    }

    public void validateEmail(EmailDTO emailDTO) {
        Member member = memberRepository.findByUserEmail(emailDTO.getEmail());

        if (member != null) {
            throw new BadRequestEmailException("가입된 이메일입니다");
        }

        if (!pendingEmail.add(emailDTO.getEmail())) {
            throw new IllegalStateException("이메일이 이미 인증 대기 중입니다.");
        }
    }

    public boolean isPendingEmail(String email) {
        return pendingEmail.contains(email);
    }

    public void removePendingEmail(String email) {
        pendingEmail.remove(email);
    }

    public boolean isExisingPhoneNumber(String phoneNumber) {
        return adminPhoneNumberRepository.existsByPhoneNumber(phoneNumber);
    }

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }
}
