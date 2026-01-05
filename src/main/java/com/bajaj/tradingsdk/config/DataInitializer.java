package com.bajaj.tradingsdk.config;

import com.bajaj.tradingsdk.model.Instrument;
import com.bajaj.tradingsdk.model.InstrumentType;
import com.bajaj.tradingsdk.model.PortfolioHolding;
import com.bajaj.tradingsdk.service.InstrumentService;
import com.bajaj.tradingsdk.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Initializes sample data on application startup
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final InstrumentService instrumentService;
    private final PortfolioService portfolioService;
    
    private static final String MOCK_USER_ID = "USER001";
    
    @Override
    public void run(String... args) {
        log.info("Initializing sample data...");
        
        initializeInstruments();
        initializePortfolio();
        
        log.info("Sample data initialization complete!");
        log.info("===========================================");
        log.info("Trading SDK is ready!");
        log.info("Swagger UI: http://localhost:8080/swagger-ui.html");
        log.info("API Docs: http://localhost:8080/api-docs");
        log.info("===========================================");
    }
    
    private void initializeInstruments() {
        log.info("Loading sample instruments...");
        
        // NSE Stocks
        instrumentService.saveInstrument(Instrument.builder()
                .symbol("RELIANCE").exchange("NSE").instrumentType(InstrumentType.EQUITY)
                .lastTradedPrice(2456.75).build());
        
        instrumentService.saveInstrument(Instrument.builder()
                .symbol("TCS").exchange("NSE").instrumentType(InstrumentType.EQUITY)
                .lastTradedPrice(3890.50).build());
        
        instrumentService.saveInstrument(Instrument.builder()
                .symbol("INFY").exchange("NSE").instrumentType(InstrumentType.EQUITY)
                .lastTradedPrice(1567.25).build());
        
        instrumentService.saveInstrument(Instrument.builder()
                .symbol("HDFC").exchange("NSE").instrumentType(InstrumentType.EQUITY)
                .lastTradedPrice(1678.90).build());
        
        instrumentService.saveInstrument(Instrument.builder()
                .symbol("ICICIBANK").exchange("NSE").instrumentType(InstrumentType.EQUITY)
                .lastTradedPrice(945.30).build());
        
        instrumentService.saveInstrument(Instrument.builder()
                .symbol("WIPRO").exchange("NSE").instrumentType(InstrumentType.EQUITY)
                .lastTradedPrice(412.80).build());
        
        instrumentService.saveInstrument(Instrument.builder()
                .symbol("HCLTECH").exchange("NSE").instrumentType(InstrumentType.EQUITY)
                .lastTradedPrice(1234.50).build());
        
        instrumentService.saveInstrument(Instrument.builder()
                .symbol("SBIN").exchange("NSE").instrumentType(InstrumentType.EQUITY)
                .lastTradedPrice(567.25).build());
        
        instrumentService.saveInstrument(Instrument.builder()
                .symbol("BAJFINANCE").exchange("NSE").instrumentType(InstrumentType.EQUITY)
                .lastTradedPrice(6789.00).build());
        
        instrumentService.saveInstrument(Instrument.builder()
                .symbol("TATAMOTORS").exchange("NSE").instrumentType(InstrumentType.EQUITY)
                .lastTradedPrice(678.45).build());
        
        // BSE Stocks
        instrumentService.saveInstrument(Instrument.builder()
                .symbol("RELIANCE").exchange("BSE").instrumentType(InstrumentType.EQUITY)
                .lastTradedPrice(2455.50).build());
        
        instrumentService.saveInstrument(Instrument.builder()
                .symbol("TCS").exchange("BSE").instrumentType(InstrumentType.EQUITY)
                .lastTradedPrice(3888.25).build());
        
        // ETFs
        instrumentService.saveInstrument(Instrument.builder()
                .symbol("NIFTYBEES").exchange("NSE").instrumentType(InstrumentType.ETF)
                .lastTradedPrice(245.60).build());
        
        instrumentService.saveInstrument(Instrument.builder()
                .symbol("BANKBEES").exchange("NSE").instrumentType(InstrumentType.ETF)
                .lastTradedPrice(432.15).build());
        
        instrumentService.saveInstrument(Instrument.builder()
                .symbol("GOLDBEES").exchange("NSE").instrumentType(InstrumentType.ETF)
                .lastTradedPrice(52.80).build());
        
        log.info("Loaded {} instruments", instrumentService.getAllInstruments().size());
    }
    
    private void initializePortfolio() {
        log.info("Loading sample portfolio holdings...");
        
        // Add some initial holdings for the mock user
        portfolioService.saveHolding(PortfolioHolding.builder()
                .symbol("RELIANCE").exchange("NSE").quantity(50)
                .averagePrice(2400.00).userId(MOCK_USER_ID).build());
        
        portfolioService.saveHolding(PortfolioHolding.builder()
                .symbol("TCS").exchange("NSE").quantity(25)
                .averagePrice(3750.00).userId(MOCK_USER_ID).build());
        
        portfolioService.saveHolding(PortfolioHolding.builder()
                .symbol("INFY").exchange("NSE").quantity(100)
                .averagePrice(1500.00).userId(MOCK_USER_ID).build());
        
        portfolioService.saveHolding(PortfolioHolding.builder()
                .symbol("NIFTYBEES").exchange("NSE").quantity(200)
                .averagePrice(240.00).userId(MOCK_USER_ID).build());
        
        log.info("Loaded {} portfolio holdings", portfolioService.getPortfolio(MOCK_USER_ID).size());
    }
}
