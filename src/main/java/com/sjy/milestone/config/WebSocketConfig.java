package com.sjy.milestone.config;

import com.sjy.milestone.SocketHandler.Redis_Socket.ChatSocketHandler;
import com.sjy.milestone.SocketHandler.Redis_Socket.CommentNoticeSocketHandler;
import com.sjy.milestone.SocketHandler.Redis_Socket.OfflineHandler;
import com.sjy.milestone.SocketHandler.Redis_Socket.OrderStatusSocketHandler;
import com.sjy.milestone.SocketHandler.Socket.RequestResponseHandler;
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