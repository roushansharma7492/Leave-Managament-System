package com.leavemgmt.exception;

/**
 * UnauthorizedException - Exception thrown when user is not authorized
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
