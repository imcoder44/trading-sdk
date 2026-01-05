package com.bajaj.tradingsdk.repository;

import com.bajaj.tradingsdk.model.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory repository for Orders
 */
@Repository
public class OrderRepository {
    
    private final Map<String, Order> orders = new ConcurrentHashMap<>();
    
    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }
    
    public Optional<Order> findById(String orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }
    
    public List<Order> findByUserId(String userId) {
        return orders.values().stream()
                .filter(order -> order.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
    
    public Order save(Order order) {
        orders.put(order.getOrderId(), order);
        return order;
    }
    
    public boolean existsById(String orderId) {
        return orders.containsKey(orderId);
    }
    
    public void deleteAll() {
        orders.clear();
    }
}
