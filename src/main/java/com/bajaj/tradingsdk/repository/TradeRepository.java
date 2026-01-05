package com.bajaj.tradingsdk.repository;

import com.bajaj.tradingsdk.model.Trade;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory repository for Trades
 */
@Repository
public class TradeRepository {
    
    private final Map<String, Trade> trades = new ConcurrentHashMap<>();
    
    public List<Trade> findAll() {
        return new ArrayList<>(trades.values());
    }
    
    public Optional<Trade> findById(String tradeId) {
        return Optional.ofNullable(trades.get(tradeId));
    }
    
    public List<Trade> findByUserId(String userId) {
        return trades.values().stream()
                .filter(trade -> trade.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
    
    public List<Trade> findByOrderId(String orderId) {
        return trades.values().stream()
                .filter(trade -> trade.getOrderId().equals(orderId))
                .collect(Collectors.toList());
    }
    
    public Trade save(Trade trade) {
        trades.put(trade.getTradeId(), trade);
        return trade;
    }
    
    public void deleteAll() {
        trades.clear();
    }
}
