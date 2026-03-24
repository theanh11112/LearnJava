package com.example.demo.controller;

import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ErrorResponse;
import com.example.demo.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice  // ← ĐÃ CÓ @ResponseBody MẶC ĐỊNH
@Slf4j
public class GlobalExceptionHandlerWithRestControllerAdvice {
    
    // ========== 1. XỬ LÝ LỖI 404 - KHÔNG cần @ResponseBody ==========
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(ResourceNotFoundException ex, WebRequest request) {
        log.warn("[REST_CONTROLLER_ADVICE] Resource not found: {}", ex.getMessage());
        
        return new ErrorResponse(
            "NOT_FOUND",
            ex.getMessage(),
            LocalDateTime.now().toString(),
            request.getDescription(false).replace("uri=", "")
        );
        // ↑ Tự động thành JSON, không cần @ResponseBody
    }
    
    // ========== 2. XỬ LÝ LỖI VALIDATION - KHÔNG cần @ResponseBody ==========
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
        log.warn("[REST_CONTROLLER_ADVICE] Validation error: {}", ex.getMessage());
        
        Map<String, Object> response = new HashMap<>();
        response.put("handler", "REST_CONTROLLER_ADVICE (toàn cục)");
        response.put("type", "VALIDATION_ERROR");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("path", request.getDescription(false).replace("uri=", ""));
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        response.put("errors", errors);
        
        return response;  // ← Tự động thành JSON
    }
    
    // ========== 3. XỬ LÝ LỖI BUSINESS - KHÔNG cần @ResponseBody ==========
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBusiness(BusinessException ex, WebRequest request) {
        log.warn("[REST_CONTROLLER_ADVICE] Business error: {}", ex.getMessage());
        
        return new ErrorResponse(
            ex.getCode(),
            ex.getMessage(),
            LocalDateTime.now().toString(),
            request.getDescription(false).replace("uri=", "")
        );
    }
    
    // ========== 4. XỬ LÝ LỖI RUNTIME - KHÔNG cần @ResponseBody ==========
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRuntime(RuntimeException ex, WebRequest request) {
        log.error("[REST_CONTROLLER_ADVICE] Runtime error: {}", ex.getMessage());
        
        return new ErrorResponse(
            "RUNTIME_ERROR",
            ex.getMessage(),
            LocalDateTime.now().toString(),
            request.getDescription(false).replace("uri=", "")
        );
    }
    
    // ========== 5. VẪN CÓ THỂ DÙNG RESPONSEENTITY NẾU MUỐN ==========
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, WebRequest request) {
        log.error("[REST_CONTROLLER_ADVICE] Unhandled exception: ", ex);
        
        ErrorResponse error = new ErrorResponse(
            "INTERNAL_SERVER_ERROR",
            "Đã có lỗi xảy ra, vui lòng thử lại sau",
            LocalDateTime.now().toString(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}