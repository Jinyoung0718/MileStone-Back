package com.sjy.milestone.sockethandler.socket;

import com.sjy.milestone.session.WebsocketSessionManager;
import com.sjy.milestone.sockethandler.AbstractSocketHandler;
import org.springframework.stereotype.Component;

@Component
public class RequestResponseHandler extends AbstractSocketHandler {

    public RequestResponseHandler(WebsocketSessionManager websocketSessionManager) {
        super(websocketSessionManager, "ws/chat/notification");
    }
}