package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_name", nullable = false, length = 100)
    private String name;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private Integer age;
    
    // ========== THÊM 2 FIELD NÀY ==========
    
    @Column(length = 15)
    private String phoneNumber;
    
    @Column
    private LocalDate birthDate;
    
    // ========== KẾT THÚC ==========
    
    @Column(nullable = false)
    private String status;  // ACTIVE, INACTIVE, BANNED
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @Transient
    private String temporaryData;  // Không lưu vào DB
    
    // Quan hệ 1-nhiều với Order
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Order> orders = new ArrayList<>();
}