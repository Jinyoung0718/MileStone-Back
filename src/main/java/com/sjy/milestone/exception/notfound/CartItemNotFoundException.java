package com.sjy.milestone.exception.notfound;

public class CartItemNotFoundException extends NotFoundException {
    public CartItemNotFoundException(String message) {
        super(message);
    }
}