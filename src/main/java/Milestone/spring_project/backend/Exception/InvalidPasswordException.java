package Milestone.spring_project.backend.Exception;

public class InvalidPasswordException extends  RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
