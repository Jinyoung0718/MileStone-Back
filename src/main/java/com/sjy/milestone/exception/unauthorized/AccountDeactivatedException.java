package com.sjy.milestone.exception.unauthorized;

public class AccountDeactivatedException extends UnAuthorized {
    public AccountDeactivatedException(String message) {
        super(message);
    }
}

