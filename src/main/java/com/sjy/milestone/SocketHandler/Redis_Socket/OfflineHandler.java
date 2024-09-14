package com.sjy.milestone.SocketHandler.Redis_Socket;

import com.sjy.milestone.Exception.SessionNotFoundException;
import com.sjy.milestone.session.SessionManager;
import com.sjy.milestone.session.WebsocketSessionManager;
import com.sjy.milestone.Util.WebSocketUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component @RequiredArgsConstructor
public class OfflineHandler extends TextWebSocketHandler {

    private static final String PATH = "ws/offline";

    private final SessionManager sessionManager;
    private final WebsocketSessionManager websocketSessionManager;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        String sessionId = WebSocketUtil.extractSessionIdFromCookies(session);
        if (sessionManager.isValidSession(sessionId)) {
            String email = sessionManager.getSession(sessionId);
            websocketSessionManager.addSession(email, PATH, session);
        } else {
            throw new SessionNotFoundException("유효하지 않은 세션 값");
        }
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        if (websocketSessionManager.isSessionPresent(session.getId())) {
            websocketSessionManager.removeSessionFromAllPaths(session.getId());
        }
    }

    @SuppressWarnings("unused")
    public void handleMessage(String message) {
        String targetEmail = WebSocketUtil.extractEmailFromMessage(message);
        websocketSessionManager.sendMessageToMember(PATH, targetEmail, message);
    }
}
