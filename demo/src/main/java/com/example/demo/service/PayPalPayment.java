package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PayPalPayment implements PaymentService {
    
    public PayPalPayment() {
        log.info("PayPalPayment instance created!");
    }
    
    @Override
    public String processPayment(double amount) {
        log.info("Processing ${} via PayPal", amount);
        return "SUCCESS: Paid $" + amount + " via PayPal";
    }
    
    @Override
    public String getProviderName() {
        return "PayPal";
    }
}