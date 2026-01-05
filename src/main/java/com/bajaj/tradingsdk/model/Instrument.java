package com.bajaj.tradingsdk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a tradable financial instrument
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Instrument {
    
    private String symbol;          // e.g., "RELIANCE", "TCS"
    private String exchange;        // e.g., "NSE", "BSE"
    private InstrumentType instrumentType;  // EQUITY, ETF, etc.
    private Double lastTradedPrice; // Current market price
}
