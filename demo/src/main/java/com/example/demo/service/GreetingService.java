package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service  // ← Đánh dấu đây là Bean
public class GreetingService {
    
    public String sayHello(String name) {
        return "Hello, " + name + "!";
    }
    
    public String sayGoodbye(String name) {
        return "Goodbye, " + name + "!";
    }
}