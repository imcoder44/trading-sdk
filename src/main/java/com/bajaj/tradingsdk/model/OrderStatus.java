package com.bajaj.tradingsdk.model;

/**
 * Enum representing the status of an order
 */
public enum OrderStatus {
    NEW,        // Order just created
    PLACED,     // Order placed in the system
    EXECUTED,   // Order fully executed
    CANCELLED   // Order cancelled
}
