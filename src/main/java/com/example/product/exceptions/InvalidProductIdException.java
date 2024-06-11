package com.example.product.exceptions;

public class InvalidProductIdException extends Throwable {
    public InvalidProductIdException(String s) {
        super(s);
    }
}