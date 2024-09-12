package Milestone.spring_project.backend.Exception;

public class WebSocketMessageException extends RuntimeException {
    public WebSocketMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}