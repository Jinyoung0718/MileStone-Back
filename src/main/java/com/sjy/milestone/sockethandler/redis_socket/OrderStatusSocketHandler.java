package com.sjy.milestone.sockethandler.redis_socket;

import com.sjy.milestone.sockethandler.AbstractSocketHandler;
import com.sjy.milestone.session.WebsocketSessionManager;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusSocketHandler extends AbstractSocketHandler {

    public OrderStatusSocketHandler(WebsocketSessionManager websocketSessionManager) {
        super(websocketSessionManager, "ws/order-status");
    }
}