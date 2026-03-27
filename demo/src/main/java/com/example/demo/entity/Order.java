package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String orderNumber;
    
    private BigDecimal amount;
    
    private String status;  // PENDING, PROCESSING, COMPLETED, CANCELLED
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    private LocalDateTime processedAt;
    
    // Quan hệ nhiều-1 với User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}