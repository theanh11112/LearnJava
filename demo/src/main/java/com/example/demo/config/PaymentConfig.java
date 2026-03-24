package com.example.demo.config;

import com.example.demo.service.PaymentService;
import com.example.demo.service.PayPalPayment;
import com.example.demo.service.StripePayment;
import com.example.demo.service.VNPayPayment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class PaymentConfig {
    
    // ✅ Đổi tên Bean thành "stripe" (khớp với @Qualifier trong Controller)
    @Bean(name = "stripe")
    @ConditionalOnProperty(name = "payment.provider", havingValue = "stripe")
    public PaymentService stripePayment() {
        log.info("Creating StripePayment Bean with name 'stripe'");
        return new StripePayment();
    }
    
    // ✅ Đổi tên Bean thành "paypal"
    @Bean(name = "paypal")
    @ConditionalOnProperty(name = "payment.provider", havingValue = "paypal")
    public PaymentService paypalPayment() {
        log.info("Creating PayPalPayment Bean with name 'paypal'");
        return new PayPalPayment();
    }
    
    // ✅ Đổi tên Bean thành "vnpay"
    @Bean(name = "vnpay")
    @ConditionalOnProperty(name = "payment.provider", havingValue = "vnpay")
    public PaymentService vnpayPayment() {
        log.info("Creating VNPayPayment Bean with name 'vnpay'");
        return new VNPayPayment();
    }
    
    // ✅ Đổi tên Bean thành "default"
    @Bean(name = "default")
    @ConditionalOnProperty(name = "payment.provider", havingValue = "default", matchIfMissing = true)
    public PaymentService defaultPayment() {
        log.info("Creating Default Payment Bean with name 'default'");
        return new PayPalPayment();
    }
}