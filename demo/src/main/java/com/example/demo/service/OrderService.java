package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.repository.OrderRepository;

import io.micrometer.common.lang.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    
    private final OrderRepository orderRepository;
    
    /**
     * VÍ DỤ 1: TIMEOUT SAU 5 GIÂY
     * Nếu method chạy quá 5 giây, tự động rollback
     */
    @Transactional(timeout = 5)  // ← 5 giây
    public Order createOrderWithTimeout(String orderNumber, BigDecimal amount) {
        log.info("=== BẮT ĐẦU TẠO ĐƠN HÀNG ===");
        log.info("Order: {}, Amount: {}", orderNumber, amount);
        
        // Tạo order
        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setAmount(amount);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        log.info("Order created, ID: {}", savedOrder.getId());
        
        // GIẢ SỬ XỬ LÝ LÂU (4 giây) - VẪN TRONG TIMEOUT
        log.info("Processing order... (4 seconds)");
        try {
            Thread.sleep(4000);  // 4 giây
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        savedOrder.setStatus("PROCESSING");
        savedOrder.setProcessedAt(LocalDateTime.now());
        orderRepository.save(savedOrder);
        
        log.info("Order processed successfully222");
        return savedOrder;
    }
    
    /**
     * VÍ DỤ 2: TIMEOUT BỊ VƯỢT QUÁ
     * Method này sẽ bị timeout và rollback
     */
    @Transactional(timeout = 3)  // ← 3 giây
    public Order createOrderWithTimeoutExceeded(String orderNumber, BigDecimal amount) {
        log.info("=== BẮT ĐẦU TẠO ĐƠN HÀNG (SẼ TIMEOUT) ===");
        log.info("Order: {}, Amount: {}", orderNumber, amount);
        
        // Tạo order
        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setAmount(amount);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        log.info("Order created, ID: {}", savedOrder.getId());
        
        // GIẢ SỬ XỬ LÝ LÂU (5 giây) - VƯỢT TIMEOUT
        log.info("Processing order... (5 seconds) - WILL TIMEOUT!");
        try {
            Thread.sleep(5000);  // 5 giây > timeout 3 giây
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Code này sẽ không bao giờ chạy vì timeout
        savedOrder.setStatus("PROCESSING");
        savedOrder.setProcessedAt(LocalDateTime.now());
        orderRepository.save(savedOrder);
        
        log.info("This line will never be reached");
        return savedOrder;
    }
    
    /**
     * VÍ DỤ 3: TIMEOUT VỚI readOnly = true
     */
    @Transactional(readOnly = true, timeout = 10)
    public Order getOrderById(@NonNull Long id) {
        log.info("Fetching order with id: {}", id);
        
        // Giả sử query phức tạp mất 2 giây
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}