package com.bajaj.tradingsdk.exception;

/**
 * Exception thrown when there's insufficient holdings for a sell order
 */
public class InsufficientHoldingsException extends RuntimeException {
    
    public InsufficientHoldingsException(String message) {
        super(message);
    }
}
