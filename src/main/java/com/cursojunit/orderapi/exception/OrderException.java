package com.cursojunit.orderapi.exception;

public class OrderException extends Exception {

    public OrderException(String message) {
        super(message);
    }

    public OrderException(String message, Throwable e) {
        super(message, e);
    }

}
