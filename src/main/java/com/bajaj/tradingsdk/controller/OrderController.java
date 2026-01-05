package com.bajaj.tradingsdk.controller;

import com.bajaj.tradingsdk.dto.ApiResponse;
import com.bajaj.tradingsdk.dto.OrderRequest;
import com.bajaj.tradingsdk.dto.OrderResponse;
import com.bajaj.tradingsdk.model.Order;
import com.bajaj.tradingsdk.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Order Management APIs
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "2. Order Management APIs", description = "APIs for placing and managing orders")
public class OrderController {
    
    private final OrderService orderService;
    
    /**
     * POST /api/v1/orders - Place a new order
     */
    @PostMapping
    @Operation(summary = "Place a new order", description = "Place a new BUY or SELL order (MARKET or LIMIT)")
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(@Valid @RequestBody OrderRequest request) {
        log.info("POST /api/v1/orders - Placing order: {} {} {} shares of {}", 
                request.getOrderType(), request.getOrderStyle(), request.getQuantity(), request.getSymbol());
        
        Order order = orderService.placeOrder(request);
        OrderResponse response = OrderResponse.fromOrder(order);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order placed successfully", response));
    }
    
    /**
     * GET /api/v1/orders/{orderId} - Fetch order status
     */
    @GetMapping("/{orderId}")
    @Operation(summary = "Get order by ID", description = "Fetch order details and status by order ID")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable String orderId) {
        log.info("GET /api/v1/orders/{} - Fetching order", orderId);
        
        Order order = orderService.getOrder(orderId);
        OrderResponse response = OrderResponse.fromOrder(order);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * GET /api/v1/orders - Fetch all orders
     */
    @GetMapping
    @Operation(summary = "Get all orders", description = "Fetch all orders for the current user")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {
        log.info("GET /api/v1/orders - Fetching all orders");
        
        List<OrderResponse> orders = orderService.getAllOrders().stream()
                .map(OrderResponse::fromOrder)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success("Fetched " + orders.size() + " orders", orders));
    }
    
    /**
     * DELETE /api/v1/orders/{orderId} - Cancel an order
     */
    @DeleteMapping("/{orderId}")
    @Operation(summary = "Cancel an order", description = "Cancel a pending order by order ID")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(@PathVariable String orderId) {
        log.info("DELETE /api/v1/orders/{} - Cancelling order", orderId);
        
        Order order = orderService.cancelOrder(orderId);
        OrderResponse response = OrderResponse.fromOrder(order);
        
        return ResponseEntity.ok(ApiResponse.success("Order cancelled successfully", response));
    }
}
