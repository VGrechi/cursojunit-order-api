package com.cursojunit.orderapi.exception;

public class ControlBalanceException extends Exception {

    public ControlBalanceException(String message) {
        super(message);
    }

    public ControlBalanceException(String message, Throwable e) {
        super(message, e);
    }

}
