package com.bajaj.tradingsdk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a portfolio holding
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioHolding {
    
    private String symbol;
    private String exchange;
    private Integer quantity;
    private Double averagePrice;        // Average buy price
    private Double currentPrice;        // Current market price
    private Double currentValue;        // quantity * currentPrice
    private Double profitLoss;          // currentValue - (quantity * averagePrice)
    private Double profitLossPercentage;
    private String userId;
}
