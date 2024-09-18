package com.sjy.milestone.sockethandler.socket;

import com.sjy.milestone.exception.notfound.SessionNotFoundException;
import com.sjy.milestone.session.WebsocketSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component @RequiredArgsConstructor @Slf4j
public class RequestResponseHandler extends TextWebSocketHandler {

    private static final String PATH = "ws/chat/notifications";
    private final WebsocketSessionManager websocketSessionManager;

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SessionNotFoundException("유효하지 않은 세션 값");
        }

        String userEmail = authentication.getName();
        websocketSessionManager.addSession(userEmail, PATH, session);
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status)  {
        if (websocketSessionManager.isSessionPresent(session.getId())) {
            websocketSessionManager.removeSessionFromAllPaths(session.getId());
            log.info("세션 ID {}가 경로 {}에서 제거되었습니다.", session.getId(), PATH);
        }
    }
}
