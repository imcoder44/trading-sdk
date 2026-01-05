package com.bajaj.tradingsdk.exception;

/**
 * Exception thrown when an order operation fails
 */
public class OrderException extends RuntimeException {
    
    public OrderException(String message) {
        super(message);
    }
}
