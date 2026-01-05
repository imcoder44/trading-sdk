package com.bajaj.tradingsdk.dto;

import com.bajaj.tradingsdk.model.Order;
import com.bajaj.tradingsdk.model.OrderStatus;
import com.bajaj.tradingsdk.model.OrderStyle;
import com.bajaj.tradingsdk.model.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for order response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    
    private String orderId;
    private String symbol;
    private String exchange;
    private OrderType orderType;
    private OrderStyle orderStyle;
    private Integer quantity;
    private Double price;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static OrderResponse fromOrder(Order order) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .symbol(order.getSymbol())
                .exchange(order.getExchange())
                .orderType(order.getOrderType())
                .orderStyle(order.getOrderStyle())
                .quantity(order.getQuantity())
                .price(order.getPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}
