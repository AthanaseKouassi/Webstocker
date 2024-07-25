package com.webstocker.exception;

public class InvalideDateFormatException extends RuntimeException {
    public InvalideDateFormatException(String message, Throwable cause) {
        super(message, cause);
    }

}
