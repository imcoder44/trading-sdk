package com.bajaj.tradingsdk.controller;

import com.bajaj.tradingsdk.dto.ApiResponse;
import com.bajaj.tradingsdk.model.PortfolioHolding;
import com.bajaj.tradingsdk.service.OrderService;
import com.bajaj.tradingsdk.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Portfolio APIs
 */
@RestController
@RequestMapping("/api/v1/portfolio")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "4. Portfolio APIs", description = "APIs for fetching portfolio holdings")
public class PortfolioController {
    
    private final PortfolioService portfolioService;
    private final OrderService orderService;
    
    /**
     * GET /api/v1/portfolio - Fetch current portfolio holdings
     */
    @GetMapping
    @Operation(summary = "Get portfolio holdings", description = "Fetch current portfolio holdings for the user")
    public ResponseEntity<ApiResponse<List<PortfolioHolding>>> getPortfolio() {
        log.info("GET /api/v1/portfolio - Fetching portfolio");
        
        String userId = orderService.getMockUserId();
        List<PortfolioHolding> holdings = portfolioService.getPortfolio(userId);
        
        return ResponseEntity.ok(ApiResponse.success("Fetched " + holdings.size() + " holdings", holdings));
    }
    
    /**
     * GET /api/v1/portfolio/{symbol} - Fetch specific holding
     */
    @GetMapping("/{symbol}")
    @Operation(summary = "Get holding by symbol", description = "Fetch specific portfolio holding by symbol")
    public ResponseEntity<ApiResponse<PortfolioHolding>> getHolding(
            @PathVariable String symbol,
            @RequestParam(defaultValue = "NSE") String exchange) {
        log.info("GET /api/v1/portfolio/{} - Fetching holding from {}", symbol, exchange);
        
        String userId = orderService.getMockUserId();
        
        return portfolioService.getHolding(userId, symbol, exchange)
                .map(holding -> ResponseEntity.ok(ApiResponse.success(holding)))
                .orElse(ResponseEntity.notFound().build());
    }
}
