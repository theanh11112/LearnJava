package com.example.demo.repository;

import com.example.demo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // Tìm order theo user
    List<Order> findByUserId(Long userId);
    
    // Tìm order theo status
    List<Order> findByStatus(String status);
    
    // Tìm order theo amount lớn hơn
    List<Order> findByAmountGreaterThan(BigDecimal amount);
    
    // Tìm order trong khoảng thời gian
    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    // JPQL: Tìm order theo user và status
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.status = :status")
    List<Order> findByUserIdAndStatus(@Param("userId") Long userId, 
                                       @Param("status") String status);
    
    // JPQL: Tổng tiền của user
    @Query("SELECT SUM(o.amount) FROM Order o WHERE o.user.id = :userId AND o.status = 'COMPLETED'")
    BigDecimal getTotalSpentByUser(@Param("userId") Long userId);
    
    // JPQL: Đếm số order theo status
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    long countByStatus(@Param("status") String status);
    
    // Native SQL: Lấy order mới nhất của user
    @Query(value = "SELECT * FROM orders WHERE user_id = :userId ORDER BY created_at DESC LIMIT 1", 
           nativeQuery = true)
    Order findLatestOrderByUser(@Param("userId") Long userId);
    
    // UPDATE: Cập nhật status order
    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.status = :status WHERE o.id = :id")
    int updateOrderStatus(@Param("id") Long id, @Param("status") String status);
    
    // DELETE: Xóa order cũ
    @Modifying
    @Transactional
    @Query("DELETE FROM Order o WHERE o.createdAt < :date AND o.status = 'CANCELLED'")
    int deleteOldCancelledOrders(@Param("date") LocalDateTime date);
}