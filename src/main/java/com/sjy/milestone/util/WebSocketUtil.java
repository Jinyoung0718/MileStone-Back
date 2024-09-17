package com.sjy.milestone.util;

import com.sjy.milestone.exception.notfound.SessionNotFoundException;
import com.sjy.milestone.session.SesssionConst;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import java.util.List;
import java.util.Map;

@Component @RequiredArgsConstructor
public class WebSocketUtil {

    public static String extractSessionIdFromCookies(WebSocketSession session) {
        List<String> cookies = session.getHandshakeHeaders().get("Cookie");
        if (cookies != null) {
            for (String cookieHeader : cookies) {
                String[] individualCookies = cookieHeader.split("; ");
                for (String cookie : individualCookies) {
                    if (cookie.startsWith(SesssionConst.SESSION_COOKIE_NAME + "=")) {
                        return cookie.substring(SesssionConst.SESSION_COOKIE_NAME.length() + 1);
                    }
                }
            }
        }
        throw new SessionNotFoundException("세션 값을 찾을 수 없습니다");
    }

    public static String extractEmailFromMessageForChat(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> messageData = objectMapper.readValue(message, new TypeReference<>() {
            });
            return (String) messageData.get("recipientEmail");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("메시지에서 이메일 추출 중 오류 발생", e);
        }
    }

    public static String extractEmailFromMessage(String message) {
        return message.split(":")[0];
    }

}