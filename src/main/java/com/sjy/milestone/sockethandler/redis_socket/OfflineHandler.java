package com.sjy.milestone.sockethandler.redis_socket;

import com.sjy.milestone.session.WebsocketSessionManager;
import com.sjy.milestone.sockethandler.AbstractSocketHandler;
import org.springframework.stereotype.Component;

@Component
public class OfflineHandler extends AbstractSocketHandler {

    public OfflineHandler(WebsocketSessionManager websocketSessionManager) {
        super(websocketSessionManager, "ws/offline");
    }
}
