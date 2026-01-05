package com.bajaj.tradingsdk.controller;

import com.bajaj.tradingsdk.dto.ApiResponse;
import com.bajaj.tradingsdk.model.Instrument;
import com.bajaj.tradingsdk.service.InstrumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Instrument APIs
 */
@RestController
@RequestMapping("/api/v1/instruments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "1. Instrument APIs", description = "APIs for fetching tradable instruments")
public class InstrumentController {
    
    private final InstrumentService instrumentService;
    
    /**
     * GET /api/v1/instruments - Fetch all tradable instruments
     */
    @GetMapping
    @Operation(summary = "Get all instruments", description = "Fetch list of all tradable instruments")
    public ResponseEntity<ApiResponse<List<Instrument>>> getAllInstruments() {
        log.info("GET /api/v1/instruments - Fetching all instruments");
        List<Instrument> instruments = instrumentService.getAllInstruments();
        return ResponseEntity.ok(ApiResponse.success("Fetched " + instruments.size() + " instruments", instruments));
    }
    
    /**
     * GET /api/v1/instruments/{symbol} - Fetch specific instrument
     */
    @GetMapping("/{symbol}")
    @Operation(summary = "Get instrument by symbol", description = "Fetch a specific instrument by symbol and exchange")
    public ResponseEntity<ApiResponse<Instrument>> getInstrument(
            @PathVariable String symbol,
            @RequestParam(defaultValue = "NSE") String exchange) {
        log.info("GET /api/v1/instruments/{} - Fetching instrument from {}", symbol, exchange);
        
        return instrumentService.getInstrument(symbol, exchange)
                .map(instrument -> ResponseEntity.ok(ApiResponse.success(instrument)))
                .orElse(ResponseEntity.notFound().build());
    }
}
