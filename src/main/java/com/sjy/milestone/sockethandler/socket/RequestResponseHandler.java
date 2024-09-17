package com.sjy.milestone.sockethandler.socket;

import com.sjy.milestone.exception.notfound.SessionNotFoundException;
import com.sjy.milestone.session.SessionManager;
import com.sjy.milestone.session.WebsocketSessionManager;
import com.sjy.milestone.util.WebSocketUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component @RequiredArgsConstructor @Slf4j
public class RequestResponseHandler extends TextWebSocketHandler {

    private final WebsocketSessionManager websocketSessionManager;
    private final SessionManager sessionManager;
    private static final String PATH = "ws/chat/notifications";

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) {
        String sessionId = WebSocketUtil.extractSessionIdFromCookies(session);
        if (sessionManager.isValidSession(sessionId)) {
            String email = sessionManager.getSession(sessionId);
            websocketSessionManager.addSession(email, PATH, session);
        } else {
            throw new SessionNotFoundException("유효하지 않은 세션 값");
        }
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status)  {
        if (websocketSessionManager.isSessionPresent(session.getId())) {
            websocketSessionManager.removeSessionFromAllPaths(session.getId());
            log.info("세션 ID {}가 경로 {}에서 제거되었습니다.", session.getId(), PATH);
        }
    }
}
