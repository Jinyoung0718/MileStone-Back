package com.sjy.milestone.exception.badrequest;

abstract public class BadRequest extends RuntimeException {
    public BadRequest(String message) {
        super(message);
    }
}
