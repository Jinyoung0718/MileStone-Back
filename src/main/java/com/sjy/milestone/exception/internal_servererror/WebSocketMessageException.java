package com.sjy.milestone.exception.internal_servererror;

public class WebSocketMessageException extends InternalServerError {
    public WebSocketMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}