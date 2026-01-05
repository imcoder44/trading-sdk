package com.bajaj.tradingsdk.service;

import com.bajaj.tradingsdk.model.OrderType;
import com.bajaj.tradingsdk.model.Trade;
import com.bajaj.tradingsdk.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service for managing trades
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TradeService {
    
    private final TradeRepository tradeRepository;
    
    /**
     * Get all trades for a user
     */
    public List<Trade> getTradesForUser(String userId) {
        log.debug("Fetching trades for user: {}", userId);
        return tradeRepository.findByUserId(userId);
    }
    
    /**
     * Get all trades
     */
    public List<Trade> getAllTrades() {
        return tradeRepository.findAll();
    }
    
    /**
     * Get trades for a specific order
     */
    public List<Trade> getTradesForOrder(String orderId) {
        return tradeRepository.findByOrderId(orderId);
    }
    
    /**
     * Create a new trade (when order is executed)
     */
    public Trade createTrade(String orderId, String symbol, String exchange, 
                             OrderType tradeType, int quantity, double executionPrice, String userId) {
        log.debug("Creating trade for order: {}", orderId);
        
        Trade trade = Trade.builder()
                .tradeId("TRD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .orderId(orderId)
                .symbol(symbol)
                .exchange(exchange)
                .tradeType(tradeType)
                .quantity(quantity)
                .executionPrice(executionPrice)
                .totalValue(quantity * executionPrice)
                .executedAt(LocalDateTime.now())
                .userId(userId)
                .build();
        
        return tradeRepository.save(trade);
    }
}
