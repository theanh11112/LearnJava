package com.example.demo.exception;

public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " không tìm thấy với id: " + id);
    }
    
    public ResourceNotFoundException(String resourceName, String field, String value) {
        super(resourceName + " không tìm thấy với " + field + ": " + value);
    }
}