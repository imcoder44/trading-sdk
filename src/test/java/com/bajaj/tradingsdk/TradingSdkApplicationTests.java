package com.bajaj.tradingsdk;

import com.bajaj.tradingsdk.dto.OrderRequest;
import com.bajaj.tradingsdk.model.*;
import com.bajaj.tradingsdk.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TradingSdkApplicationTests {
    
    @Autowired
    private InstrumentService instrumentService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private TradeService tradeService;
    
    @Autowired
    private PortfolioService portfolioService;
    
    @Test
    void contextLoads() {
        assertNotNull(instrumentService);
        assertNotNull(orderService);
        assertNotNull(tradeService);
        assertNotNull(portfolioService);
    }
    
    @Test
    @DisplayName("Should fetch all instruments")
    void testGetAllInstruments() {
        List<Instrument> instruments = instrumentService.getAllInstruments();
        assertNotNull(instruments);
        assertFalse(instruments.isEmpty());
    }
    
    @Test
    @DisplayName("Should get specific instrument")
    void testGetInstrument() {
        var instrument = instrumentService.getInstrument("RELIANCE", "NSE");
        assertTrue(instrument.isPresent());
        assertEquals("RELIANCE", instrument.get().getSymbol());
        assertEquals("NSE", instrument.get().getExchange());
    }
    
    @Test
    @DisplayName("Should place a BUY MARKET order successfully")
    void testPlaceBuyMarketOrder() {
        OrderRequest request = OrderRequest.builder()
                .symbol("WIPRO")
                .exchange("NSE")
                .orderType(OrderType.BUY)
                .orderStyle(OrderStyle.MARKET)
                .quantity(10)
                .build();
        
        Order order = orderService.placeOrder(request);
        
        assertNotNull(order);
        assertNotNull(order.getOrderId());
        assertEquals(OrderStatus.EXECUTED, order.getStatus());
        assertEquals("WIPRO", order.getSymbol());
        assertEquals(10, order.getQuantity());
    }
    
    @Test
    @DisplayName("Should place a BUY LIMIT order successfully")
    void testPlaceBuyLimitOrder() {
        OrderRequest request = OrderRequest.builder()
                .symbol("HCLTECH")
                .exchange("NSE")
                .orderType(OrderType.BUY)
                .orderStyle(OrderStyle.LIMIT)
                .quantity(5)
                .price(1200.00)
                .build();
        
        Order order = orderService.placeOrder(request);
        
        assertNotNull(order);
        assertEquals(OrderStatus.PLACED, order.getStatus());
        assertEquals(1200.00, order.getPrice());
    }
    
    @Test
    @DisplayName("Should fetch order by ID")
    void testGetOrder() {
        OrderRequest request = OrderRequest.builder()
                .symbol("SBIN")
                .exchange("NSE")
                .orderType(OrderType.BUY)
                .orderStyle(OrderStyle.MARKET)
                .quantity(20)
                .build();
        
        Order createdOrder = orderService.placeOrder(request);
        Order fetchedOrder = orderService.getOrder(createdOrder.getOrderId());
        
        assertEquals(createdOrder.getOrderId(), fetchedOrder.getOrderId());
    }
    
    @Test
    @DisplayName("Should fetch portfolio holdings")
    void testGetPortfolio() {
        String userId = orderService.getMockUserId();
        List<PortfolioHolding> holdings = portfolioService.getPortfolio(userId);
        
        assertNotNull(holdings);
        assertFalse(holdings.isEmpty());
    }
    
    @Test
    @DisplayName("Should fetch trades after order execution")
    void testGetTrades() {
        String userId = orderService.getMockUserId();
        
        // Place a market order (which gets executed immediately)
        OrderRequest request = OrderRequest.builder()
                .symbol("TATAMOTORS")
                .exchange("NSE")
                .orderType(OrderType.BUY)
                .orderStyle(OrderStyle.MARKET)
                .quantity(15)
                .build();
        
        orderService.placeOrder(request);
        
        List<Trade> trades = tradeService.getTradesForUser(userId);
        assertNotNull(trades);
        assertFalse(trades.isEmpty());
    }
}
