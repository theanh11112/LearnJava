
package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VNPayPayment implements PaymentService {
    
    public VNPayPayment() {
        log.info("VNPayPayment instance created!");
    }
    
    @Override
    public String processPayment(double amount) {
        log.info("Processing ${} via VNPay", amount);
        return "SUCCESS: Paid $" + amount + " via VNPay";
    }
    
    @Override
    public String getProviderName() {
        return "VNPay";
    }
}