package com.sjy.milestone.exception.badrequest;

public class InvalidPasswordException extends  BadRequest {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
