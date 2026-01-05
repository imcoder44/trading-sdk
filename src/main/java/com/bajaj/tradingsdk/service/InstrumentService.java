package com.bajaj.tradingsdk.service;

import com.bajaj.tradingsdk.model.Instrument;
import com.bajaj.tradingsdk.repository.InstrumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing financial instruments
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InstrumentService {
    
    private final InstrumentRepository instrumentRepository;
    
    /**
     * Get all available instruments
     */
    public List<Instrument> getAllInstruments() {
        log.debug("Fetching all instruments");
        return instrumentRepository.findAll();
    }
    
    /**
     * Get instrument by symbol and exchange
     */
    public Optional<Instrument> getInstrument(String symbol, String exchange) {
        log.debug("Fetching instrument: {} on {}", symbol, exchange);
        return instrumentRepository.findBySymbolAndExchange(symbol, exchange);
    }
    
    /**
     * Check if instrument exists
     */
    public boolean instrumentExists(String symbol, String exchange) {
        return instrumentRepository.existsBySymbolAndExchange(symbol, exchange);
    }
    
    /**
     * Get current price for an instrument
     */
    public Double getCurrentPrice(String symbol, String exchange) {
        return instrumentRepository.findBySymbolAndExchange(symbol, exchange)
                .map(Instrument::getLastTradedPrice)
                .orElse(null);
    }
    
    /**
     * Add or update an instrument
     */
    public Instrument saveInstrument(Instrument instrument) {
        log.debug("Saving instrument: {}", instrument.getSymbol());
        return instrumentRepository.save(instrument);
    }
}
