package com.sjy.milestone.exception.badrequest;

public class CartItemAlreadyExistsException extends BadRequest {
    public CartItemAlreadyExistsException(String message) {
        super(message);
    }
}