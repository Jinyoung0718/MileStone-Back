package Milestone.spring_project.backend.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;

@Configuration
public class RedisStreamConfig {

    @Bean
    public StreamOperations<String, String, String> streamOperations(RedisTemplate<String, String> redisTemplate) {
        return redisTemplate.opsForStream();
    }
}
