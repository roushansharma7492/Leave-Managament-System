package com.leavemgmt.exception;

/**
 * BusinessLogicException - Exception thrown for business logic violations
 */
public class BusinessLogicException extends RuntimeException {
    public BusinessLogicException(String message) {
        super(message);
    }

    public BusinessLogicException(String message, Throwable cause) {
        super(message, cause);
    }
}
