package com.example.demo.dto;

import com.example.demo.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    
    private Long id;
    private String name;
    private String email;
    private Integer age;
    private String phoneNumber;
    
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
    
    private String status;
    
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // Factory method
    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .age(user.getAge())
            .phoneNumber(user.getPhoneNumber())
            .birthDate(user.getBirthDate())
            .status(user.getStatus())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();
    }
}