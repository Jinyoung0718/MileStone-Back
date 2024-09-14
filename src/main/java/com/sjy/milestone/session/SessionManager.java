package com.sjy.milestone.session;

import com.sjy.milestone.Exception.SessionNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.UUID;

@Component @RequiredArgsConstructor @Slf4j
public class SessionManager {

    private final RedisTemplate<String, String> redisTemplate;

    public String createSession(String userEmail) {
        String sessionId = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(sessionId, userEmail, Duration.ofHours(1));
        return sessionId;
    }

    public boolean isValidSession(String sessionId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(sessionId));
    }

    public String getSession(String sessionId) {
        String userEmail = redisTemplate.opsForValue().get(sessionId);
        if (userEmail == null) {
            log.error("세션 ID {}에 해당하는 이메일을 찾을 수 없습니다.", sessionId);
            throw new SessionNotFoundException("유효하지 않은 세션입니다.");
        }
        return userEmail;
    }


    public void removeSession(String sessionId) {
        redisTemplate.delete(sessionId);
    }
}
