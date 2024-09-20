package com.sjy.milestone.sockethandler;

import com.sjy.milestone.exception.notfound.SessionNotFoundException;
import com.sjy.milestone.session.WebsocketSessionManager;
import com.sjy.milestone.util.WebSocketUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@RequiredArgsConstructor
public abstract class AbstractSocketHandler extends TextWebSocketHandler {

    private final WebsocketSessionManager websocketSessionManager;
    private final String path;


    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SessionNotFoundException("유효하지 않은 세션 값");
        }

        String userEmail = authentication.getName();
        websocketSessionManager.addSession(userEmail, path, session);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        if (websocketSessionManager.isSessionPresent(session.getId())) {
            websocketSessionManager.removeSessionFromAllPaths(session.getId());
        }
    }

    @SuppressWarnings("unused")
    public void handleMessage(String message){
        String targetEmail = WebSocketUtil.extractEmailFromMessage(message);
        getWebsocketSessionManager().sendMessageToMember(getPath(), targetEmail, message);
    }

    protected WebsocketSessionManager getWebsocketSessionManager() {
        return websocketSessionManager;
    }

    protected String getPath() {
        return path;
    }
}

