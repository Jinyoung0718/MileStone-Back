package Milestone.spring_project.backend.Exception;

public class PaymentCancellationException extends RuntimeException {

    public PaymentCancellationException(String message) {
        super(message);
    }

    public PaymentCancellationException(String message, Throwable cause) {
        super(message, cause);
    }
}