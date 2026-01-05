package com.bajaj.tradingsdk.dto;

import com.bajaj.tradingsdk.model.OrderStyle;
import com.bajaj.tradingsdk.model.OrderType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new order
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    
    @NotBlank(message = "Symbol is required")
    private String symbol;
    
    @NotBlank(message = "Exchange is required")
    private String exchange;
    
    @NotNull(message = "Order type is required (BUY/SELL)")
    private OrderType orderType;
    
    @NotNull(message = "Order style is required (MARKET/LIMIT)")
    private OrderStyle orderStyle;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;
    
    private Double price;  // Required for LIMIT orders
}
