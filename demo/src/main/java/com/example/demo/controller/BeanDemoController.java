package com.example.demo.controller;

import com.example.demo.service.GreetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
@Slf4j
public class BeanDemoController {
    
    private final GreetingService greetingService;  // ← Spring tự inject Bean vào đây
    
    @GetMapping("/hello")
    public String hello(@RequestParam(defaultValue = "World") String name) {
        log.info("Calling hello with name: {}", name);
        return greetingService.sayHello(name);
    }
    
    @GetMapping("/goodbye")
    public String goodbye(@RequestParam(defaultValue = "World") String name) {
        log.info("Calling goodbye with name: {}", name);
        return greetingService.sayGoodbye(name);
    }
}