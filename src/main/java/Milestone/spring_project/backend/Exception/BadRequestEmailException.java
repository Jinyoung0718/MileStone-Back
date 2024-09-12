package Milestone.spring_project.backend.Exception;

public class BadRequestEmailException extends RuntimeException {
    public BadRequestEmailException(String message) {
        super(message);
    }
}