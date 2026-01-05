package com.bajaj.tradingsdk.service;

import com.bajaj.tradingsdk.model.PortfolioHolding;
import com.bajaj.tradingsdk.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing portfolio holdings
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PortfolioService {
    
    private final PortfolioRepository portfolioRepository;
    private final InstrumentService instrumentService;
    
    /**
     * Get all portfolio holdings for a user
     */
    public List<PortfolioHolding> getPortfolio(String userId) {
        log.debug("Fetching portfolio for user: {}", userId);
        List<PortfolioHolding> holdings = portfolioRepository.findByUserId(userId);
        
        // Update current values based on latest prices
        holdings.forEach(this::updateCurrentValue);
        
        return holdings;
    }
    
    /**
     * Get specific holding
     */
    public Optional<PortfolioHolding> getHolding(String userId, String symbol, String exchange) {
        return portfolioRepository.findByUserIdAndSymbolAndExchange(userId, symbol, exchange);
    }
    
    /**
     * Add to portfolio (on BUY)
     */
    public PortfolioHolding addToPortfolio(String userId, String symbol, String exchange, 
                                           int quantity, double price) {
        log.debug("Adding to portfolio: {} {} shares of {} at {}", userId, quantity, symbol, price);
        
        Optional<PortfolioHolding> existingHolding = 
                portfolioRepository.findByUserIdAndSymbolAndExchange(userId, symbol, exchange);
        
        PortfolioHolding holding;
        if (existingHolding.isPresent()) {
            // Update existing holding - calculate new average price
            holding = existingHolding.get();
            int newQuantity = holding.getQuantity() + quantity;
            double newAveragePrice = ((holding.getQuantity() * holding.getAveragePrice()) + 
                                      (quantity * price)) / newQuantity;
            holding.setQuantity(newQuantity);
            holding.setAveragePrice(newAveragePrice);
        } else {
            // Create new holding
            holding = PortfolioHolding.builder()
                    .symbol(symbol)
                    .exchange(exchange)
                    .quantity(quantity)
                    .averagePrice(price)
                    .userId(userId)
                    .build();
        }
        
        updateCurrentValue(holding);
        return portfolioRepository.save(holding);
    }
    
    /**
     * Remove from portfolio (on SELL)
     */
    public PortfolioHolding removeFromPortfolio(String userId, String symbol, String exchange, 
                                                 int quantity) {
        log.debug("Removing from portfolio: {} {} shares of {}", userId, quantity, symbol);
        
        Optional<PortfolioHolding> existingHolding = 
                portfolioRepository.findByUserIdAndSymbolAndExchange(userId, symbol, exchange);
        
        if (existingHolding.isEmpty()) {
            return null;
        }
        
        PortfolioHolding holding = existingHolding.get();
        int newQuantity = holding.getQuantity() - quantity;
        
        if (newQuantity <= 0) {
            // Remove holding completely
            portfolioRepository.delete(userId, symbol, exchange);
            return null;
        } else {
            holding.setQuantity(newQuantity);
            updateCurrentValue(holding);
            return portfolioRepository.save(holding);
        }
    }
    
    /**
     * Check if user has enough holdings for a sell order
     */
    public boolean hasEnoughHoldings(String userId, String symbol, String exchange, int quantity) {
        return portfolioRepository.findByUserIdAndSymbolAndExchange(userId, symbol, exchange)
                .map(holding -> holding.getQuantity() >= quantity)
                .orElse(false);
    }
    
    /**
     * Get current holding quantity
     */
    public int getHoldingQuantity(String userId, String symbol, String exchange) {
        return portfolioRepository.findByUserIdAndSymbolAndExchange(userId, symbol, exchange)
                .map(PortfolioHolding::getQuantity)
                .orElse(0);
    }
    
    /**
     * Update current value based on latest price
     */
    private void updateCurrentValue(PortfolioHolding holding) {
        Double currentPrice = instrumentService.getCurrentPrice(holding.getSymbol(), holding.getExchange());
        if (currentPrice != null) {
            holding.setCurrentPrice(currentPrice);
            holding.setCurrentValue(holding.getQuantity() * currentPrice);
            double investedValue = holding.getQuantity() * holding.getAveragePrice();
            holding.setProfitLoss(holding.getCurrentValue() - investedValue);
            holding.setProfitLossPercentage((holding.getProfitLoss() / investedValue) * 100);
        }
    }
    
    /**
     * Save a portfolio holding directly
     */
    public PortfolioHolding saveHolding(PortfolioHolding holding) {
        updateCurrentValue(holding);
        return portfolioRepository.save(holding);
    }
}
