package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // ========== 1. QUERY METHOD - Spring tự implement ==========
    
    // Tìm theo email
    Optional<User> findByEmail(String email);
    
    // Kiểm tra email tồn tại
    boolean existsByEmail(String email);
    
    // Tìm theo tên chứa keyword (không phân biệt hoa thường)
    List<User> findByNameContainingIgnoreCase(String name);
    
    // Tìm theo độ tuổi trong khoảng
    List<User> findByAgeBetween(Integer minAge, Integer maxAge);
    
    // Tìm theo status và phân trang
    Page<User> findByStatus(String status, Pageable pageable);
    
    // Tìm top 5 user mới nhất
    List<User> findTop5ByOrderByCreatedAtDesc();
    
    // Đếm số user theo status
    long countByStatus(String status);
    
    // ========== 2. JPQL QUERY ==========
    
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmailJPQL(@Param("email") String email);
    
    @Query("SELECT u FROM User u WHERE u.name LIKE %:keyword% OR u.email LIKE %:keyword%")
    List<User> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT u FROM User u WHERE u.createdAt BETWEEN :start AND :end")
    List<User> findByCreatedDateRange(@Param("start") LocalDateTime start, 
                                       @Param("end") LocalDateTime end);
    
    @Query("SELECT u FROM User u WHERE u.age >= :minAge AND u.age <= :maxAge")
    List<User> findByAgeRange(@Param("minAge") int minAge, @Param("maxAge") int maxAge);
    
    @Query("SELECT u FROM User u WHERE u.status = :status ORDER BY u.createdAt DESC")
    List<User> findActiveUsersOrderByCreatedAt(@Param("status") String status);
    
    // ========== 3. NATIVE SQL QUERY ==========
    
    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    Optional<User> findByEmailNative(@Param("email") String email);
    
    @Query(value = "SELECT * FROM users WHERE age >= :minAge", nativeQuery = true)
    List<User> findUsersByAgeNative(@Param("minAge") int minAge);
    
    // ========== 4. UPDATE QUERY ==========
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.status = :status WHERE u.id = :id")
    int updateUserStatus(@Param("id") Long id, @Param("status") String status);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.name = :name WHERE u.id = :id")
    int updateUserName(@Param("id") Long id, @Param("name") String name);
    
    // ========== 5. DELETE QUERY ==========
    
    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.status = :status AND u.updatedAt < :date")
    int deleteInactiveUsers(@Param("status") String status, @Param("date") LocalDateTime date);
    
    // ========== 6. COUNT QUERY ==========
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.status = :status")
    long countByStatusJPQL(@Param("status") String status);
    
    // ========== 7. ENTITY GRAPH (Tránh N+1 query) ==========
    // ✅ ĐÃ SỬA: Đổi tên method để không override JpaRepository.findById
    @Override
    @EntityGraph(attributePaths = {"orders"})
    @NonNull
    Optional<User> findById(@NonNull Long id); 

    @EntityGraph(attributePaths = {"orders"})
    List<User> findByStatus(String status);
}