package com.sjy.milestone.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component @RequiredArgsConstructor
public class WebSocketUtil {

    public static String extractEmailFromMessageForChat(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> messageData = objectMapper.readValue(message, new TypeReference<>() {
            });
            if (messageData.containsKey("recipientEmail")) {
                return (String) messageData.get("recipientEmail");
            } else {
                throw new IllegalArgumentException("잘못된 메시지 형식: recipientEmail 필드가 없습니다.");
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("메시지에서 이메일 추출 중 오류 발생", e);
        }
    }

    public static String extractEmailFromMessage(String message) {
        return message.split(":")[0];
    }
}