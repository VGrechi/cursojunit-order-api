package com.cursojunit.orderapi.exception;

public class ResponseError {

    private String exception;
    private Integer httpStatus;
    private String message;

    /*
     * Constructor
     */
    public ResponseError(String exception, Integer httpStatus, String message) {
        this.exception = exception;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    /*
     * Getters and Setters
     */
    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
