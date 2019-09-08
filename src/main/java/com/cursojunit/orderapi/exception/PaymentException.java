package com.cursojunit.orderapi.exception;

public class PaymentException extends Exception {

    public PaymentException(String message) {
        super(message);
    }

    public PaymentException(String message, Throwable e) {
        super(message, e);
    }

}
