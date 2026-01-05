package com.bajaj.tradingsdk.controller;

import com.bajaj.tradingsdk.dto.ApiResponse;
import com.bajaj.tradingsdk.model.Trade;
import com.bajaj.tradingsdk.service.OrderService;
import com.bajaj.tradingsdk.service.TradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Trade APIs
 */
@RestController
@RequestMapping("/api/v1/trades")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "3. Trade APIs", description = "APIs for fetching executed trades")
public class TradeController {
    
    private final TradeService tradeService;
    private final OrderService orderService;
    
    /**
     * GET /api/v1/trades - Fetch all executed trades for the user
     */
    @GetMapping
    @Operation(summary = "Get all trades", description = "Fetch list of all executed trades for the current user")
    public ResponseEntity<ApiResponse<List<Trade>>> getAllTrades() {
        log.info("GET /api/v1/trades - Fetching all trades");
        
        String userId = orderService.getMockUserId();
        List<Trade> trades = tradeService.getTradesForUser(userId);
        
        return ResponseEntity.ok(ApiResponse.success("Fetched " + trades.size() + " trades", trades));
    }
    
    /**
     * GET /api/v1/trades/{tradeId} - Fetch specific trade
     */
    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get trades by order ID", description = "Fetch all trades for a specific order")
    public ResponseEntity<ApiResponse<List<Trade>>> getTradesByOrder(@PathVariable String orderId) {
        log.info("GET /api/v1/trades/order/{} - Fetching trades for order", orderId);
        
        List<Trade> trades = tradeService.getTradesForOrder(orderId);
        
        return ResponseEntity.ok(ApiResponse.success("Fetched " + trades.size() + " trades", trades));
    }
}
