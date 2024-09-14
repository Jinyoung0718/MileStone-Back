package com.sjy.milestone.Exception;

public class NotFindMember extends  RuntimeException {
    public NotFindMember (String message) {
        super(message);
    }
}