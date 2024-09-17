package com.sjy.milestone.exception.internal_servererror;

public class PaymentCancellationException extends InternalServerError {

    public PaymentCancellationException(String message) {
        super(message);
    }

    public PaymentCancellationException(String message, Throwable cause) {
        super(message, cause);
    }
}