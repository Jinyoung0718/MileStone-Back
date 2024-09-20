package com.sjy.milestone.sockethandler.redis_socket;

import com.sjy.milestone.sockethandler.AbstractSocketHandler;
import com.sjy.milestone.session.WebsocketSessionManager;
import org.springframework.stereotype.Component;

@Component
public class CommentNoticeSocketHandler extends AbstractSocketHandler {

    public CommentNoticeSocketHandler(WebsocketSessionManager websocketSessionManager) {
        super(websocketSessionManager, "ws/comment-notice");
    }
}