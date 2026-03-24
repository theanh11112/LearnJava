// package com.example.demo.controller;

// import com.example.demo.exception.ResourceNotFoundException;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.validation.FieldError;
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.ResponseBody;
// import org.springframework.web.bind.annotation.ResponseStatus;

// import java.time.LocalDateTime;
// import java.util.HashMap;
// import java.util.Map;

// @ControllerAdvice  // ← KHÔNG có @ResponseBody mặc định
// @Slf4j
// public class GlobalExceptionHandlerWithControllerAdvice {
    
//     // ========== 1. XỬ LÝ LỖI 404 - Cần @ResponseBody để trả JSON ==========
//     @ExceptionHandler(ResourceNotFoundException.class)
//     @ResponseStatus(HttpStatus.NOT_FOUND)
//     @ResponseBody  // ← BẮT BUỘC nếu muốn trả JSON
//     public Map<String, Object> handleNotFound(ResourceNotFoundException ex) {
//         log.warn("[CONTROLLER_ADVICE] Resource not found: {}", ex.getMessage());

//         ex.printStackTrace();
        
//         Map<String, Object> response = new HashMap<>();
//         response.put("handler", "CONTROLLER_ADVICE (toàn cục)");
//         response.put("type", "NOT_FOUND");
//         response.put("message", ex.getMessage());
//         response.put("timestamp", LocalDateTime.now());
        
//         return response;  // ← Cần @ResponseBody để thành JSON
//     }
    
//     // ========== 2. XỬ LÝ LỖI VALIDATION - Cũng cần @ResponseBody ==========
//     @ExceptionHandler(MethodArgumentNotValidException.class)
//     @ResponseStatus(HttpStatus.BAD_REQUEST)
//     @ResponseBody  // ← BẮT BUỘC
//     public Map<String, Object> handleValidation(MethodArgumentNotValidException ex) {
//         log.warn("[CONTROLLER_ADVICE] Validation error: {}", ex.getMessage());
//         ex.printStackTrace();
//         Map<String, Object> response = new HashMap<>();
//         response.put("handler", "CONTROLLER_ADVICE (toàn cục)");
//         response.put("type", "VALIDATION_ERROR");
        
//         Map<String, String> errors = new HashMap<>();
//         ex.getBindingResult().getAllErrors().forEach((error) -> {
//             String fieldName = ((FieldError) error).getField();
//             String errorMessage = error.getDefaultMessage();
//             errors.put(fieldName, errorMessage);
//         });
//         response.put("errors", errors);
//         response.put("timestamp", LocalDateTime.now());
        
//         return response;
//     }
    
//     // ========== 3. XỬ LÝ LỖI RUNTIME - Dùng ResponseEntity (cách khác) ==========
//     @ExceptionHandler(RuntimeException.class)
//     public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
//         log.warn("[CONTROLLER_ADVICE] RuntimeException: {}", ex.getMessage());
//         ex.printStackTrace();
//         Map<String, Object> response = new HashMap<>();
//         response.put("handler", "CONTROLLER_ADVICE (toàn cục111)");
//         response.put("type", "RUNTIME_ERROR");
//         response.put("message", ex.getMessage());
//         response.put("timestamp", LocalDateTime.now());
        
//         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//         // ↑ ResponseEntity tự động trả JSON, không cần @ResponseBody
//     }
    
//     // ========== 4. VÍ DỤ TRẢ VỀ HTML (nếu cần) ==========
//     // @ExceptionHandler(SomeException.class)
//     // public String handleHtmlError() {
//     //     return "error/500";  // → Trả về file HTML, không phải JSON
//     // }
// }