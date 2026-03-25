package com.example.demo.controller;

import com.example.demo.entity.Order;
import com.example.demo.exception.DatabaseException;
import com.example.demo.service.TransactionTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionTestController {
    
    private final TransactionTestService transactionTestService;
    
    // Test 1: RuntimeException - Rollback
    @PostMapping("/test1")
    public ResponseEntity<String> test1(@RequestParam String orderNumber) {
        log.info("=== TEST 1: RuntimeException sẽ rollback ===");
        try {
            transactionTestService.testRuntimeExceptionRollback(orderNumber);
            return ResponseEntity.ok("Success (should not reach here)");
        } catch (Exception e) {
            return ResponseEntity.ok("TEST 1 RESULT: Rollback occurred - " + e.getMessage());
        }
    }
    
    // Test 2: Checked Exception - No rollback
    @PostMapping("/test2")
    public ResponseEntity<String> test2(@RequestParam String orderNumber) {
        log.info("=== TEST 2: Checked Exception sẽ KHÔNG rollback ===");
        try {
            transactionTestService.testCheckedExceptionNoRollback(orderNumber);
            return ResponseEntity.ok("Success (should not reach here)");
        } catch (DatabaseException e) {
            return ResponseEntity.ok("TEST 2 RESULT: No rollback - " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.ok("Unexpected error: " + e.getMessage());
        }
    }
    
    // Test 3: Checked Exception với rollbackFor - Rollback
    @PostMapping("/test3")
    public ResponseEntity<String> test3(@RequestParam String orderNumber) {
        log.info("=== TEST 3: Checked Exception với rollbackFor sẽ rollback ===");
        try {
            transactionTestService.testCheckedExceptionWithRollbackFor(orderNumber);
            return ResponseEntity.ok("Success (should not reach here)");
        } catch (DatabaseException e) {
            return ResponseEntity.ok("TEST 3 RESULT: Rollback occurred - " + e.getMessage());
        }
    }
    
    // Test 4: RuntimeException với noRollbackFor - No rollback
    @PostMapping("/test4")
    public ResponseEntity<String> test4(@RequestParam String orderNumber) {
        log.info("=== TEST 4: RuntimeException với noRollbackFor sẽ KHÔNG rollback ===");
        try {
            transactionTestService.testRuntimeExceptionWithNoRollbackFor(orderNumber);
            return ResponseEntity.ok("Success (should not reach here)");
        } catch (Exception e) {
            return ResponseEntity.ok("TEST 4 RESULT: No rollback - " + e.getMessage());
        }
    }
    
    // Test 5: try-catch - No rollback
    @PostMapping("/test5")
    public ResponseEntity<String> test5(@RequestParam String orderNumber) {
        log.info("=== TEST 5: try-catch sẽ ngăn rollback ===");
        Order order = transactionTestService.testTryCatchNoRollback(orderNumber);
        return ResponseEntity.ok("TEST 5 RESULT: Success, order saved with ID: " + order.getId());
    }
    
    // Test 6: Mixed exceptions
    @PostMapping("/test6")
    public ResponseEntity<String> test6(@RequestParam String orderNumber) {
        log.info("=== TEST 6: Mixed exceptions ===");
        try {
            transactionTestService.testMixedExceptions(orderNumber);
            return ResponseEntity.ok("Success (should not reach here)");
        } catch (DatabaseException e) {
            return ResponseEntity.ok("TEST 6 RESULT: Rollback occurred due to DatabaseException");
        }
    }
}