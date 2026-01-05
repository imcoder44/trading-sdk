package com.bajaj.tradingsdk.service;

import com.bajaj.tradingsdk.dto.OrderRequest;
import com.bajaj.tradingsdk.exception.InsufficientHoldingsException;
import com.bajaj.tradingsdk.exception.OrderException;
import com.bajaj.tradingsdk.exception.ResourceNotFoundException;
import com.bajaj.tradingsdk.exception.ValidationException;
import com.bajaj.tradingsdk.model.*;
import com.bajaj.tradingsdk.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service for managing orders
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final InstrumentService instrumentService;
    private final TradeService tradeService;
    private final PortfolioService portfolioService;
    
    // Mocked user ID (as per assignment - single hardcoded user)
    private static final String MOCK_USER_ID = "USER001";
    
    /**
     * Place a new order
     */
    public Order placeOrder(OrderRequest request) {
        log.info("Placing order: {} {} {} shares of {}", 
                request.getOrderType(), request.getOrderStyle(), request.getQuantity(), request.getSymbol());
        
        // Validate the order
        validateOrder(request);
        
        // Get current market price
        Double marketPrice = instrumentService.getCurrentPrice(request.getSymbol(), request.getExchange());
        if (marketPrice == null) {
            throw new ResourceNotFoundException("Instrument", "symbol", request.getSymbol());
        }
        
        // Determine execution price
        Double executionPrice = request.getOrderStyle() == OrderStyle.MARKET 
                ? marketPrice 
                : request.getPrice();
        
        // Create the order
        Order order = Order.builder()
                .orderId("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .symbol(request.getSymbol().toUpperCase())
                .exchange(request.getExchange().toUpperCase())
                .orderType(request.getOrderType())
                .orderStyle(request.getOrderStyle())
                .quantity(request.getQuantity())
                .price(executionPrice)
                .status(OrderStatus.NEW)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .userId(MOCK_USER_ID)
                .build();
        
        // Save the order
        order = orderRepository.save(order);
        log.info("Order created with ID: {}", order.getOrderId());
        
        // Simulate order placement
        order.setStatus(OrderStatus.PLACED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
        
        // For MARKET orders, execute immediately (simulation)
        if (request.getOrderStyle() == OrderStyle.MARKET) {
            executeOrder(order, marketPrice);
        }
        
        return order;
    }
    
    /**
     * Get order by ID
     */
    public Order getOrder(String orderId) {
        log.debug("Fetching order: {}", orderId);
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId));
    }
    
    /**
     * Get all orders for current user
     */
    public List<Order> getAllOrders() {
        return orderRepository.findByUserId(MOCK_USER_ID);
    }
    
    /**
     * Cancel an order
     */
    public Order cancelOrder(String orderId) {
        log.info("Cancelling order: {}", orderId);
        
        Order order = getOrder(orderId);
        
        if (order.getStatus() == OrderStatus.EXECUTED) {
            throw new OrderException("Cannot cancel an executed order");
        }
        
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new OrderException("Order is already cancelled");
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        
        return orderRepository.save(order);
    }
    
    /**
     * Validate order request
     */
    private void validateOrder(OrderRequest request) {
        // Check if instrument exists
        if (!instrumentService.instrumentExists(request.getSymbol(), request.getExchange())) {
            throw new ResourceNotFoundException("Instrument", "symbol", 
                    request.getSymbol() + " on " + request.getExchange());
        }
        
        // Validate quantity
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new ValidationException("Quantity must be greater than 0");
        }
        
        // Validate price for LIMIT orders
        if (request.getOrderStyle() == OrderStyle.LIMIT) {
            if (request.getPrice() == null || request.getPrice() <= 0) {
                throw new ValidationException("Price is required for LIMIT orders and must be greater than 0");
            }
        }
        
        // For SELL orders, check if user has enough holdings
        if (request.getOrderType() == OrderType.SELL) {
            if (!portfolioService.hasEnoughHoldings(MOCK_USER_ID, request.getSymbol(), 
                    request.getExchange(), request.getQuantity())) {
                int currentHolding = portfolioService.getHoldingQuantity(MOCK_USER_ID, 
                        request.getSymbol(), request.getExchange());
                throw new InsufficientHoldingsException(
                        String.format("Insufficient holdings. You have %d shares of %s, but trying to sell %d", 
                                currentHolding, request.getSymbol(), request.getQuantity()));
            }
        }
    }
    
    /**
     * Execute an order (simulation)
     */
    private void executeOrder(Order order, double executionPrice) {
        log.info("Executing order: {} at price {}", order.getOrderId(), executionPrice);
        
        // Create a trade
        tradeService.createTrade(
                order.getOrderId(),
                order.getSymbol(),
                order.getExchange(),
                order.getOrderType(),
                order.getQuantity(),
                executionPrice,
                order.getUserId()
        );
        
        // Update portfolio
        if (order.getOrderType() == OrderType.BUY) {
            portfolioService.addToPortfolio(
                    order.getUserId(),
                    order.getSymbol(),
                    order.getExchange(),
                    order.getQuantity(),
                    executionPrice
            );
        } else {
            portfolioService.removeFromPortfolio(
                    order.getUserId(),
                    order.getSymbol(),
                    order.getExchange(),
                    order.getQuantity()
            );
        }
        
        // Update order status
        order.setStatus(OrderStatus.EXECUTED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
        
        log.info("Order {} executed successfully", order.getOrderId());
    }
    
    /**
     * Get mock user ID
     */
    public String getMockUserId() {
        return MOCK_USER_ID;
    }
}
