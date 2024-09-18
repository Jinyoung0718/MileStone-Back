package com.sjy.milestone.sockethandler.redis_socket;

import com.sjy.milestone.exception.notfound.SessionNotFoundException;
import com.sjy.milestone.util.WebSocketUtil;
import com.sjy.milestone.session.WebsocketSessionManager;
import lombok.NonNull;
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
public class CommentNoticeSocketHandler extends TextWebSocketHandler {

    private static final String PATH = "ws/comment-notice";
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

    // redis 에서 설정된 패턴은 "comment/notice" 이다, 만일 대댓글 알람을 구축하려고 뒤에 * 을 붙여 각 사용자마다 채널을 별개로 설정하여서 알람을 보내는 방식을 선택하였다면
    // 사용자가 많아질 수록 메모리에 부하가 많이 오게된다. 하지만 "comment/notice/*" 을 설정하지 않고 지금과 같은 패턴을 유지한 뒤, 웹소켓매니저 클래스에서 따로 메서드를 추가생성하여서
    // 세션을 통해 이메일로 직접적으로 보내게 하였으므로 *을 붙여서 얻을 수 있는 장점을 얻고 단점을 보완하였다
}