package com.cursojunit.orderapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderException.class)
    public ResponseEntity orderExceptionHandler(OrderException e){
        ResponseError responseError = new ResponseError(e.getClass().getName(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                e.getMessage());
        return new ResponseEntity<>(responseError, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity paymentExceptionHandler(PaymentException e){
        ResponseError responseError = new ResponseError(e.getClass().getName(),
                HttpStatus.BAD_GATEWAY.value(),
                e.getMessage());
        return new ResponseEntity<>(responseError, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(ControlBalanceException.class)
    public ResponseEntity contorlBalanceExceptionHandler(ControlBalanceException e){
        ResponseError responseError = new ResponseError(e.getClass().getName(),
                HttpStatus.BAD_GATEWAY.value(),
                e.getMessage());
        return new ResponseEntity<>(responseError, HttpStatus.BAD_GATEWAY);
    }
}
