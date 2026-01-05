package com.bajaj.tradingsdk.exception;

/**
 * Exception thrown when a validation fails
 */
public class ValidationException extends RuntimeException {
    
    public ValidationException(String message) {
        super(message);
    }
}
