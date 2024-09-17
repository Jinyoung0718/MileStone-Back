package com.sjy.milestone.exception.unauthorized;

abstract public class UnAuthorized extends RuntimeException {

    public UnAuthorized(String message) {
        super(message);
    }
}
