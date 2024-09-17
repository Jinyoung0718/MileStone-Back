package com.sjy.milestone.config;

import com.sjy.milestone.sockethandler.redis_socket.ChatSocketHandler;
import com.sjy.milestone.sockethandler.redis_socket.CommentNoticeSocketHandler;
import com.sjy.milestone.sockethandler.redis_socket.OfflineHandler;
import com.sjy.milestone.sockethandler.redis_socket.OrderStatusSocketHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }


    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory,
                                                        @Qualifier("orderListenerAdapter") MessageListenerAdapter orderListenerAdapter,
                                                        @Qualifier("commentListenerAdapter") MessageListenerAdapter commentListenerAdapter,
                                                        @Qualifier("chatListenerAdapter") MessageListenerAdapter chatListenerAdapter,
                                                        @Qualifier("offlineListenerAdapter") MessageListenerAdapter offlineListenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(orderListenerAdapter, new PatternTopic("order/status"));
        container.addMessageListener(commentListenerAdapter, new PatternTopic("comment/notice"));
        container.addMessageListener(chatListenerAdapter, new PatternTopic("chat/room/*"));
        container.addMessageListener(offlineListenerAdapter, new PatternTopic("offline_message/*:*"));
        return container;
    }

    @Bean(name = "orderListenerAdapter")
    public MessageListenerAdapter orderListenerAdapter(OrderStatusSocketHandler handler) {
        return new MessageListenerAdapter(handler, "handleMessage");
    }

    @Bean(name = "commentListenerAdapter")
    public MessageListenerAdapter commentListenerAdapter(CommentNoticeSocketHandler handler) {
        return new MessageListenerAdapter(handler, "handleMessage");
    }

    @Bean(name = "chatListenerAdapter")
    public MessageListenerAdapter chatListenerAdapter(ChatSocketHandler handler) {
        return new MessageListenerAdapter(handler,"handleMessage");
    }

    @Bean(name = "offlineListenerAdapter")
    public MessageListenerAdapter offlineListenerAdapter(OfflineHandler handler) {
        return new MessageListenerAdapter(handler,"handleMessage");
    }
}