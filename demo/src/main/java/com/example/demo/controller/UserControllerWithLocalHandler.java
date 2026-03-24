// package com.example.demo.controller;

// import com.example.demo.dto.UserDto;
// import com.example.demo.service.UserService;
// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.validation.FieldError;
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.annotation.*;

// import java.time.LocalDateTime;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// @RestController
// @RequestMapping("/users-local")
// @RequiredArgsConstructor
// @Slf4j
// public class UserControllerWithLocalHandler {
    
//     private final UserService userService;
    
//     // ========== API METHODS ==========
    
//     @GetMapping
//     public ResponseEntity<List<UserDto>> getAllUsers() {
//         List<UserDto> users = userService.getAllUsers();
//         return ResponseEntity.ok(users);
//     }
    
//     @GetMapping("/{id}")
//     public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
//         UserDto user = userService.getUserById(id);
//         return ResponseEntity.ok(user);
//     }
    
//     @PostMapping
//     public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
//         UserDto created = userService.createUser(userDto);
//         return ResponseEntity.status(HttpStatus.CREATED).body(created);
//     }
    
//     // ========== LOCAL EXCEPTION HANDLERS (CHỈ ÁP DỤNG CHO CONTROLLER NÀY) ==========
    
//     // 1. Xử lý lỗi RuntimeException (email trùng, user không tìm thấy...)
//     @ExceptionHandler(RuntimeException.class)
//     public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
//         log.warn("[LOCAL] RuntimeException: {}", ex.getMessage());
        
//         Map<String, Object> response = new HashMap<>();
//         response.put("handler", "LOCAL (trong Controller)");
//         response.put("type", "RUNTIME_ERROR");
//         response.put("message", ex.getMessage());
//         response.put("timestamp", LocalDateTime.now());
        
//         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//     }
    
//     // 2. Xử lý lỗi validation riêng
//     @ExceptionHandler(MethodArgumentNotValidException.class)
//     public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
//         log.warn("[LOCAL] Validation error: {}", ex.getMessage());
        
//         Map<String, Object> response = new HashMap<>();
//         response.put("handler", "LOCAL (trong Controller)");
//         response.put("type", "VALIDATION_ERROR");
        
//         Map<String, String> errors = new HashMap<>();
//         ex.getBindingResult().getAllErrors().forEach((error) -> {
//             String fieldName = ((FieldError) error).getField();
//             String errorMessage = error.getDefaultMessage();
//             errors.put(fieldName, errorMessage);
//         });
//         response.put("errors", errors);
//         response.put("timestamp", LocalDateTime.now());
        
//         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//     }
    
//     // 3. Xử lý lỗi NumberFormatException (khi id không phải số)
//     @ExceptionHandler(NumberFormatException.class)
//     public ResponseEntity<Map<String, Object>> handleNumberFormatException(NumberFormatException ex) {
//         log.warn("[LOCAL] NumberFormatException: {}", ex.getMessage());
        
//         Map<String, Object> response = new HashMap<>();
//         response.put("handler", "LOCAL (trong Controller)");
//         response.put("type", "INVALID_ID_FORMAT");
//         response.put("message", "ID phải là số");
//         response.put("timestamp", LocalDateTime.now());
        
//         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//     }
// }