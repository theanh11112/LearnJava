package com.example.demo.controller;

import com.example.demo.service.UserRepositoryTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/repository-test")
@RequiredArgsConstructor
@Slf4j
public class RepositoryTestController {
    
    private final UserRepositoryTestService testService;
    
    @PostMapping("/create-data")
    public String createTestData() {
        testService.createTestData();
        return "Test data created! Check log for details.";
    }
    
    @GetMapping("/query-method")
    public String testQueryMethod() {
        testService.testQueryMethod();
        return "Query method test completed! Check log.";
    }
    
    @GetMapping("/jpql")
    public String testJPQL() {
        testService.testJPQLQuery();
        return "JPQL test completed! Check log.";
    }
    
    @GetMapping("/native")
    public String testNative() {
        testService.testNativeQuery();
        return "Native SQL test completed! Check log.";
    }
    
    @PutMapping("/update")
    public String testUpdate() {
        testService.testUpdateDeleteQuery();
        return "Update/Delete test completed! Check log.";
    }
    
    @GetMapping("/order")
    public String testOrder() {
        testService.testOrderQuery();
        return "Order query test completed! Check log.";
    }
}