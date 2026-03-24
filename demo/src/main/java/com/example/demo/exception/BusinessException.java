package com.example.demo.exception;

import lombok.Getter;

@Getter  // ← Lombok tự sinh getter
public class BusinessException extends RuntimeException {
    
    private final String code;  // ← Thêm field code
    
    // Constructor không tham số
    public BusinessException() {
        super("Business error occurred");
        this.code = "BUSINESS_ERROR";
    }
    
    // Constructor chỉ có message
    public BusinessException(String message) {
        super(message);
        this.code = "BUSINESS_ERROR";
    }
    
    // Constructor có code và message
    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }
    
    // Constructor có message và cause
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = "BUSINESS_ERROR";
    }
    
    // Constructor đầy đủ
    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}