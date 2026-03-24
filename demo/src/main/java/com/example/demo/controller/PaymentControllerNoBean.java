package com.example.demo.controller;

import com.example.demo.service.PaymentService;
import com.example.demo.service.PayPalPayment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/payment/no-bean")
@Slf4j

public class PaymentControllerNoBean {

    private final PaymentService paymentService = new PayPalPayment();

    @PostMapping("/pay")
    public String pay(@RequestParam double amount) {
        log.info("=== NO BEAN VERSION ===");
        log.info("Current provider: {}", paymentService.getProviderName());
        return paymentService.processPayment(amount);
    }

    @GetMapping("/path")
    public String getProvider() {
        return "Current payment provider: " + paymentService.getProviderName();
    }
    
    }

