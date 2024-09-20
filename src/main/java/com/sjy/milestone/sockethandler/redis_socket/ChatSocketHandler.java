package com.sjy.milestone.sockethandler.redis_socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjy.milestone.chat.dto.RedisMessageDTO;
import com.sjy.milestone.session.WebsocketSessionManager;
import com.sjy.milestone.sockethandler.AbstractSocketHandler;
import org.springframework.stereotype.Component;

@Component
public class ChatSocketHandler extends AbstractSocketHandler {

    private final ObjectMapper objectMapper;

    public ChatSocketHandler(WebsocketSessionManager websocketSessionManager, ObjectMapper objectMapper) {
        super(websocketSessionManager, "ws/chat");
        this.objectMapper = objectMapper;
    }

    @Override
    public void handleMessage(String message) {
        try {
            RedisMessageDTO redisMessageDTO = objectMapper.readValue(message, RedisMessageDTO.class);
            String targetEmail = redisMessageDTO.getRecipientEmail();
            getWebsocketSessionManager().sendMessageToMember(getPath(), targetEmail, objectMapper.writeValueAsString(redisMessageDTO));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("직렬화 과정 중 문제가 발생하였습니다", e);
        }
    }
}

