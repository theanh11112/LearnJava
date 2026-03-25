package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.DatabaseException;
import com.example.demo.exception.EmailException;
import com.example.demo.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionTestService {
    
    private final OrderRepository orderRepository;
    
    // ========== TEST 1: RuntimeException (MẶC ĐỊNH ROLLBACK) ==========
    
    /**
     * RuntimeException mặc định sẽ ROLLBACK
     */
    @Transactional
    public Order testRuntimeExceptionRollback(String orderNumber) {
        log.info("=== TEST 1: RuntimeException sẽ rollback ===");
        
        // Tạo order 1 - sẽ bị rollback
        Order order1 = createOrder(orderNumber + "_1", new BigDecimal("100"));
        Order saved1 = orderRepository.save(order1);
        log.info("Order 1 saved with ID: {}", saved1.getId());
        
        // Ném RuntimeException
        log.info("Throwing RuntimeException...");
        throw new RuntimeException("Something went wrong!");
        
        // Code này không bao giờ chạy
        // Order order2 = createOrder(orderNumber + "_2", new BigDecimal("200"));
        // return orderRepository.save(order2);
    }
    
    // ========== TEST 2: Checked Exception (MẶC ĐỊNH KHÔNG ROLLBACK) ==========
    
    /**
     * Checked Exception mặc định KHÔNG rollback
     */
    @Transactional
    public Order testCheckedExceptionNoRollback(String orderNumber) throws DatabaseException {
        log.info("=== TEST 2: Checked Exception sẽ KHÔNG rollback ===");
        
        // Tạo order 1 - sẽ được lưu
        Order order1 = createOrder(orderNumber + "_1", new BigDecimal("100"));
        Order saved1 = orderRepository.save(order1);
        log.info("Order 1 saved with ID: {}", saved1.getId());
        
        // Ném checked exception
        log.info("Throwing DatabaseException (checked)...");
        throw new DatabaseException("Database connection failed!");
    }
    
    // ========== TEST 3: rollbackFor - BẮT CHECKED EXCEPTION ROLLBACK ==========
    
    /**
     * Dùng rollbackFor để checked exception cũng rollback
     */
    @Transactional(rollbackFor = DatabaseException.class)
    public Order testCheckedExceptionWithRollbackFor(String orderNumber) throws DatabaseException {
        log.info("=== TEST 3: Checked Exception với rollbackFor sẽ rollback ===");
        
        // Tạo order 1 - sẽ bị rollback
        Order order1 = createOrder(orderNumber + "_1", new BigDecimal("100"));
        Order saved1 = orderRepository.save(order1);
        log.info("Order 1 saved with ID: {}", saved1.getId());
        
        // Ném checked exception
        log.info("Throwing DatabaseException (checked)...");
        throw new DatabaseException("Database connection failed!");
    }
    
    // ========== TEST 4: noRollbackFor - RUNTIMEEXCEPTION KHÔNG ROLLBACK ==========
    
    /**
     * Dùng noRollbackFor để RuntimeException không rollback
     */
    @Transactional(noRollbackFor = BusinessException.class)
    public Order testRuntimeExceptionWithNoRollbackFor(String orderNumber) {
        log.info("=== TEST 4: RuntimeException với noRollbackFor sẽ KHÔNG rollback ===");
        
        // Tạo order 1 - sẽ được lưu
        Order order1 = createOrder(orderNumber + "_1", new BigDecimal("100"));
        Order saved1 = orderRepository.save(order1);
        log.info("Order 1 saved with ID: {}", saved1.getId());
        
        // Ném BusinessException (RuntimeException)
        log.info("Throwing BusinessException (RuntimeException)...");
        throw new BusinessException("BUSINESS_ERROR", "Invalid business logic");
    }
    
    // ========== TEST 5: try-catch để không ảnh hưởng transaction ==========
    
    /**
     * Dùng try-catch để bắt exception và không ảnh hưởng transaction
     */
    @Transactional
    public Order testTryCatchNoRollback(String orderNumber) {
        log.info("=== TEST 5: try-catch sẽ ngăn rollback ===");
        
        // Tạo order 1 - sẽ được lưu
        Order order1 = createOrder(orderNumber + "_1", new BigDecimal("100"));
        Order saved1 = orderRepository.save(order1);
        log.info("Order 1 saved with ID: {}", saved1.getId());
        
        // Bắt exception để không rollback
        try {
            log.info("Throwing exception but catching it...");
            throw new RuntimeException("Temporary error!");
        } catch (RuntimeException e) {
            log.warn("Exception caught: {}, transaction continues", e.getMessage());
            // Không throw lại, transaction vẫn commit
        }
        
        // Tạo order 2 - vẫn được lưu
        Order order2 = createOrder(orderNumber + "_2", new BigDecimal("200"));
        Order saved2 = orderRepository.save(order2);
        log.info("Order 2 saved with ID: {}", saved2.getId());
        
        log.info("Transaction completed successfully!");
        return saved2;
    }
    
    // ========== TEST 6: Kết hợp nhiều exception ==========
    
    /**
     * Kết hợp rollbackFor và noRollbackFor
     */
    @Transactional(
        rollbackFor = DatabaseException.class,
        noRollbackFor = EmailException.class
    )
    public Order testMixedExceptions(String orderNumber) throws DatabaseException {
        log.info("=== TEST 6: Mixed exceptions ===");
        
        // Tạo order 1
        Order order1 = createOrder(orderNumber + "_1", new BigDecimal("100"));
        Order saved1 = orderRepository.save(order1);
        log.info("Order 1 saved with ID: {}", saved1.getId());
        
        try {
            // EmailException (noRollbackFor) - KHÔNG rollback
            log.info("Throwing EmailException (noRollbackFor)...");
            throw new EmailException("Email service unavailable");
        } catch (EmailException e) {
            log.warn("EmailException caught, but transaction continues: {}", e.getMessage());
        }
        
        // Tạo order 2 - vẫn được lưu
        Order order2 = createOrder(orderNumber + "_2", new BigDecimal("200"));
        Order saved2 = orderRepository.save(order2);
        log.info("Order 2 saved with ID: {}", saved2.getId());
        
        // DatabaseException (rollbackFor) - SẼ rollback tất cả
        log.info("Throwing DatabaseException (rollbackFor)...");
        throw new DatabaseException("Database error!");
    }
    
    // ========== Helper method ==========
    
    private Order createOrder(String orderNumber, BigDecimal amount) {
        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setAmount(amount);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        return order;
    }
}