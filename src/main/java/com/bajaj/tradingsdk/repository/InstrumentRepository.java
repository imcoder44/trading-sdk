package com.bajaj.tradingsdk.repository;

import com.bajaj.tradingsdk.model.Instrument;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory repository for Instruments
 */
@Repository
public class InstrumentRepository {
    
    // Using ConcurrentHashMap for thread safety
    // Key: symbol_exchange (e.g., "RELIANCE_NSE")
    private final Map<String, Instrument> instruments = new ConcurrentHashMap<>();
    
    public List<Instrument> findAll() {
        return new ArrayList<>(instruments.values());
    }
    
    public Optional<Instrument> findBySymbolAndExchange(String symbol, String exchange) {
        String key = generateKey(symbol, exchange);
        return Optional.ofNullable(instruments.get(key));
    }
    
    public Instrument save(Instrument instrument) {
        String key = generateKey(instrument.getSymbol(), instrument.getExchange());
        instruments.put(key, instrument);
        return instrument;
    }
    
    public boolean existsBySymbolAndExchange(String symbol, String exchange) {
        return instruments.containsKey(generateKey(symbol, exchange));
    }
    
    public void deleteAll() {
        instruments.clear();
    }
    
    private String generateKey(String symbol, String exchange) {
        return symbol.toUpperCase() + "_" + exchange.toUpperCase();
    }
}
