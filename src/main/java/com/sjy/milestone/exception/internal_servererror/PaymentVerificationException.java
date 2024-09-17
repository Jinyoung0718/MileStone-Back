package com.sjy.milestone.exception.internal_servererror;

public class PaymentVerificationException extends InternalServerError {

    public PaymentVerificationException(String message) {
        super(message);
    }

    public PaymentVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
