package com.sjy.milestone.Exception;

public class WebSocketMessageException extends RuntimeException {
    public WebSocketMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}