package com.sjy.milestone.exception.conflict;

abstract public class Conflict extends RuntimeException {
    public Conflict(String message) {
        super(message);
    }
}
