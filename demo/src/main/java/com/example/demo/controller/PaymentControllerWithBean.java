package com.example.demo.controller;

import com.example.demo.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment/with-bean")
@Slf4j
public class PaymentControllerWithBean {
    
    // ✅ Không cần quyết định implementation ở đây
    @Qualifier("stripe")
    private final PaymentService paymentService;

    public PaymentControllerWithBean(@Qualifier("stripe") PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    
    @PostMapping("/pay")
    public String pay(@RequestParam double amount) {
        log.info("=== WITH BEAN VERSION ===");
        log.info("Current provider: {}", paymentService.getProviderName());
        return paymentService.processPayment(amount);
    }
    
    @GetMapping("/provider")
    public String getProvider() {
        return "Current payment provider: " + paymentService.getProviderName();
    }
}