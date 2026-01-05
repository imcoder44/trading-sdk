package com.bajaj.tradingsdk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents an executed trade
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trade {
    
    private String tradeId;
    private String orderId;             // Reference to the order
    private String symbol;
    private String exchange;
    private OrderType tradeType;        // BUY or SELL
    private Integer quantity;
    private Double executionPrice;
    private Double totalValue;          // quantity * executionPrice
    private LocalDateTime executedAt;
    private String userId;
}
