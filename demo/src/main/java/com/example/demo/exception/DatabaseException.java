package com.example.demo.exception;

// Checked Exception - mặc định KHÔNG rollback
public class DatabaseException extends Exception {
    
    public DatabaseException(String message) {
        super(message);
    }
    
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}