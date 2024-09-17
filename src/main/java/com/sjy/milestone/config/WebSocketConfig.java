package com.sjy.milestone.config;

import com.sjy.milestone.sockethandler.redis_socket.ChatSocketHandler;
import com.sjy.milestone.sockethandler.redis_socket.CommentNoticeSocketHandler;
import com.sjy.milestone.sockethandler.redis_socket.OfflineHandler;
import com.sjy.milestone.sockethandler.redis_socket.OrderStatusSocketHandler;
import com.sjy.milestone.sockethandler.socket.RequestResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration @EnableWebSocket @RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer  {

    private final OrderStatusSocketHandler orderStatusSocketHandler;
    private final ChatSocketHandler chatSocketHandler;
    private final CommentNoticeSocketHandler commentNoticeSocketHandler;
    private final RequestResponseHandler requestResponseHandler;
    private final OfflineHandler offlineHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(orderStatusSocketHandler, "ws/order-status")
                .setAllowedOrigins("*");

        registry.addHandler(commentNoticeSocketHandler, "ws/comment-notice")
                .setAllowedOrigins("*");

        registry.addHandler(chatSocketHandler, "ws/chat")
                .setAllowedOrigins("*");

        registry.addHandler(requestResponseHandler, "ws/chat/notifications")
                .setAllowedOrigins("*");

        registry.addHandler(offlineHandler, "ws/offline")
                .setAllowedOrigins("*");
    }
}