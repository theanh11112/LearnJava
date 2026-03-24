package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StripePayment implements PaymentService {
    
    public StripePayment() {
        log.info("StripePayment instance created!");
    }
    
    @Override
    public String processPayment(double amount) {
        log.info("Processing ${} via Stripe", amount);
        return "SUCCESS: Paid $" + amount + " via Stripe";
    }
    
    @Override
    public String getProviderName() {
        return "Stripe";
    }
}