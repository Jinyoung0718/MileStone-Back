package com.sjy.milestone.session;

import com.sjy.milestone.exception.internal_servererror.WebSocketMessageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

@Component @RequiredArgsConstructor @Slf4j
public class WebsocketSessionManager {

    private final RedisTemplate<String, String> redisTemplate;
    private final Map<String, Map<String, List<WebSocketSession>>> pathSocketSessionMap  = new ConcurrentHashMap<>();
    private final Map<String, Queue<String>> messageQueueMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public void addSession(String email, String path, WebSocketSession session) {
        pathSocketSessionMap.computeIfAbsent(path, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(email, k ->  Collections.synchronizedList(new ArrayList<>())).add(session);

        List<String> messages = readOfflineMessage(path, email);
        messages.forEach(message -> sendMessageToMember(path, email, message));
    }

    public void sendMessageToMember(String path, String email, String message) {
        Queue<String> messageQueue = messageQueueMap.computeIfAbsent(email, k -> new ConcurrentLinkedQueue<>());
        messageQueue.add(message);
        processNextMessage(path, email);
    }

    private void processNextMessage(String path, String email) {
        Queue<String> messageQue = messageQueueMap.get(email);
        if (messageQue == null || messageQue.isEmpty()) return;
        List<WebSocketSession> webSocketSessions = pathSocketSessionMap.getOrDefault(path, Collections.emptyMap()).get(email);

        if (webSocketSessions != null && !webSocketSessions.isEmpty()) {
            String nextMessage = messageQue.poll();
            webSocketSessions.stream().filter(WebSocketSession::isOpen).forEach(session -> {
                try {
                    if (nextMessage != null) {
                        session.sendMessage(new TextMessage(nextMessage));
                        scheduledExecutorService.schedule(() -> processNextMessage(path, email), 1, TimeUnit.SECONDS);
                    }
                } catch (IOException e) {
                    throw new WebSocketMessageException("메시지 전송 실패", e);
                }
            });
        } else {
            saveOfflineMessage(path, email, messageQue.poll());
        }
    }

    public boolean isSessionPresent(String sessionId) {
        return pathSocketSessionMap.values().stream()
                .flatMap(sessionMap -> sessionMap.values().stream())
                .flatMap(List::stream)
                .anyMatch(session -> session.getId().equals(sessionId));
    } // Map -> flatMap

    public void removeSessionFromAllPaths(String sessionId) {
        Set<String> emailsToRemove = new HashSet<>();
        pathSocketSessionMap.forEach((path, sessionMap) -> {
            sessionMap.forEach((email, sessions) -> {
                sessions.removeIf(session -> session.getId().equals(sessionId));
                if (sessions.isEmpty()) emailsToRemove.add(email);
            });
            emailsToRemove.forEach(sessionMap::remove);
            emailsToRemove.clear();
        });
    }

    public void saveOfflineMessage(String path, String email, String message) {
        if (message != null) {
            String redisKey = "offline_message:" + path + ":" + email;
            redisTemplate.opsForList().rightPush(redisKey, message);
        }
    }

    public List<String> readOfflineMessage(String path, String email) {
        String redisKey = "offline_message" + path + ":" + email;
        List<String> messages = redisTemplate.opsForList().range(redisKey, 0, -1);
        redisTemplate.delete(redisKey);
        return messages;
    }
}