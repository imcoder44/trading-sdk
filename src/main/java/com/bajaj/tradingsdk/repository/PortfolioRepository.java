package com.bajaj.tradingsdk.repository;

import com.bajaj.tradingsdk.model.PortfolioHolding;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory repository for Portfolio Holdings
 */
@Repository
public class PortfolioRepository {
    
    // Key: userId_symbol_exchange
    private final Map<String, PortfolioHolding> holdings = new ConcurrentHashMap<>();
    
    public List<PortfolioHolding> findAll() {
        return new ArrayList<>(holdings.values());
    }
    
    public List<PortfolioHolding> findByUserId(String userId) {
        return holdings.values().stream()
                .filter(holding -> holding.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
    
    public Optional<PortfolioHolding> findByUserIdAndSymbolAndExchange(String userId, String symbol, String exchange) {
        String key = generateKey(userId, symbol, exchange);
        return Optional.ofNullable(holdings.get(key));
    }
    
    public PortfolioHolding save(PortfolioHolding holding) {
        String key = generateKey(holding.getUserId(), holding.getSymbol(), holding.getExchange());
        holdings.put(key, holding);
        return holding;
    }
    
    public void delete(String userId, String symbol, String exchange) {
        String key = generateKey(userId, symbol, exchange);
        holdings.remove(key);
    }
    
    public void deleteAll() {
        holdings.clear();
    }
    
    private String generateKey(String userId, String symbol, String exchange) {
        return userId + "_" + symbol.toUpperCase() + "_" + exchange.toUpperCase();
    }
}
