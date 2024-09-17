package com.sjy.milestone.exception.badrequest;

public class InsufficientStockException extends BadRequest {
    public InsufficientStockException(String message) {
        super(message);
    }
}