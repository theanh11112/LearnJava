package com.example.demo.controller;

import com.example.demo.entity.Order;
import com.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    
    private final OrderService orderService;
    
    /**
     * Test 1: Tạo order trong timeout (4 giây, timeout 5 giây)
     * URL: POST /api/orders/create?orderNumber=ORD001&amount=100
     * Kết quả: Thành công
     */
    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(
            @RequestParam String orderNumber,
            @RequestParam BigDecimal amount) {
        log.info("API: Create order with timeout");
        Order order = orderService.createOrderWithTimeout(orderNumber, amount);
        return ResponseEntity.ok(order);
    }
    
    /**
     * Test 2: Tạo order bị timeout (5 giây, timeout 3 giây)
     * URL: POST /api/orders/create-timeout?orderNumber=ORD002&amount=200
     * Kết quả: Lỗi timeout, rollback
     */
    @PostMapping("/create-timeout")
    public ResponseEntity<Order> createOrderWithTimeout(
            @RequestParam String orderNumber,
            @RequestParam BigDecimal amount) {
        log.info("API: Create order that will timeout");
        try {
            Order order = orderService.createOrderWithTimeoutExceeded(orderNumber, amount);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Test 3: Lấy order (readOnly, timeout 10 giây)
     * URL: GET /api/orders/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        log.info("API: Get order by id: {}", id);
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }
}