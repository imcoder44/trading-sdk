package com.bajaj.tradingsdk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a trading order
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    private String orderId;
    private String symbol;
    private String exchange;
    private OrderType orderType;        // BUY or SELL
    private OrderStyle orderStyle;      // MARKET or LIMIT
    private Integer quantity;
    private Double price;               // Required for LIMIT orders
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userId;              // Mocked user ID
}
