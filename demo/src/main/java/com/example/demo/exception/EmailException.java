package com.example.demo.exception;

// RuntimeException nhưng ta muốn KHÔNG rollback
public class EmailException extends RuntimeException {
    
    public EmailException(String message) {
        super(message);
    }
}