package com.sjy.milestone.Exception;

public class PaymentCancellationException extends RuntimeException {

    public PaymentCancellationException(String message) {
        super(message);
    }

    public PaymentCancellationException(String message, Throwable cause) {
        super(message, cause);
    }
}