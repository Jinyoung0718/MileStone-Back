package com.sjy.milestone.exception.conflict;

public class EmailAlreadyExistsException extends Conflict {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
