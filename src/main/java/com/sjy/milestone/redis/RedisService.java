package com.sjy.milestone.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service @RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public boolean setIfAbsent(String key, String value, long seconds) {
        return !Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value, Duration.ofSeconds(seconds)));
    } // 키가 없으면 값을 설정하고 true 반환

    public String getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setData(String key, String value, long duration) {
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(duration));
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }
}
