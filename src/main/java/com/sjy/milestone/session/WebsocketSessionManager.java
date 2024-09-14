package com.sjy.milestone.session;

import com.sjy.milestone.Exception.WebSocketMessageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

@Component @RequiredArgsConstructor @Slf4j
public class WebsocketSessionManager {

    private final RedisTemplate<String, String> redisTemplate;
    private final Map<String, Map<String, List<WebSocketSession>>> pathSocketSessionMap  = new ConcurrentHashMap<>();
    private final Map<String, Queue<String>> messageQueueMap = new ConcurrentHashMap<>();
    // OrderStatusScheduler 에서 거의 동시에 메시지가 빡 하고 나와서, 예외가 나타난다 이를 해결하기 위해 메시지 큐를 사용하기로 함
    // 그런데 조사해보니 주문 상태 뿐만 아니라, 전체 소켓 핸들러에 적용하면 좋겠다고 생각함
    // 왜냐하면  메시지 전송 과정에서 충돌이 발생하지 않고, 채팅에는 순서가 중요하기에 적절하다고 여김


    // 웹 소켓 세션을 concurrentHashMap 만으로 충분한가? 주제로 블로그 글 첨부
    // 이메일을 최상위 키로 하지 않은 이유는 소켓 특정 경로의 작업을 처리할 때, 해당 경로의 연견된 세션을 찾기 위해 이메일을 먼저 조회해야 하는 복잡성이 생김

    public void addSession(String email, String path, WebSocketSession session) {
        pathSocketSessionMap.computeIfAbsent(path, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(email, k -> new CopyOnWriteArrayList<>()).add(session);

        List<String> messages = readOfflineMessage(path, email);
        messages.forEach(message -> sendMessageToMember(path, email, message));
    }

    public void sendMessageToMember(String path, String email, String message) {
        Queue<String> messageQueue = messageQueueMap.computeIfAbsent(email, k -> new ConcurrentLinkedQueue<>());
        messageQueue.add(message);
        processNextMessage(path, email);
    }

    private void processNextMessage(String path, String email) {
        Queue<String> messageQueue = messageQueueMap.get(email);
        if (messageQueue == null || messageQueue.isEmpty()) return;

        List<WebSocketSession> webSocketSessions = pathSocketSessionMap.getOrDefault(path, Collections.emptyMap()).get(email);
        if (webSocketSessions != null && !webSocketSessions.isEmpty()) {
            String nextMessage = messageQueue.poll(); // 큐에서 메시지 꺼내는 명령어다. 블로그 글 쓰자..

            webSocketSessions.stream().filter(WebSocketSession::isOpen).forEach(session -> {
                try {
                    if (nextMessage != null) session.sendMessage(new TextMessage(nextMessage));
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            processNextMessage(path, email);
                        }
                    }, 100); // 공부한 비동기 큐와 스레드 풀 기반 작업 방법들 블로그 정리
                } catch (IOException e) {
                    throw new WebSocketMessageException("메시지를 전송하는데 실패하였습니다", e);
                }
            });
        } else {
            saveOfflineMessage(path, email, messageQueue.poll());
        }
    }

    public boolean isSessionPresent(String sessionId) {
        for (Map<String, List<WebSocketSession>> sessionMap : pathSocketSessionMap.values()) {
            for (List<WebSocketSession> sessions : sessionMap.values()) {
                for (WebSocketSession session : sessions) {
                    if (session.getId().equals(sessionId)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

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
        String redisKey = "offline_message" + path + ":" + email;
        redisTemplate.opsForList().rightPush(redisKey, message);
    }

    // 웹 소켓 세션을 hashMap 과 redis 에 저장을 하여서, 웹소켓 세션 이용은 hashMap 을 사용하고
    // redis 를 통해, 사용자가 오프라인 일 때 온 알람을 처리하기 위해 사용함

    public List<String> readOfflineMessage(String path, String email) {
        String redisKey = "offline_message" + path + ":" + email;
        List<String> messages = redisTemplate.opsForList().range(redisKey, 0, -1);
        redisTemplate.delete(redisKey);
        return messages;
    }
}