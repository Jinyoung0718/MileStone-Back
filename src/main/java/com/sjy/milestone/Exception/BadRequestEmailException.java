package com.sjy.milestone.Exception;

public class BadRequestEmailException extends RuntimeException {
    public BadRequestEmailException(String message) {
        super(message);
    }
}