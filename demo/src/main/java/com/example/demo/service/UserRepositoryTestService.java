package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryTestService {
    
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    
    // ========== TEST 1: QUERY METHOD ==========
    @Transactional(readOnly = true)
    public void testQueryMethod() {
        log.info("=== TEST QUERY METHOD ===");
        
        // 1. Tìm theo email
        userRepository.findByEmail("test@example.com")
            .ifPresent(user -> log.info("Found user: {}", user.getName()));
        
        // 2. Kiểm tra email tồn tại
        boolean exists = userRepository.existsByEmail("test@example.com");
        log.info("Email exists: {}", exists);
        
        // 3. Tìm theo tên chứa keyword
        List<User> users = userRepository.findByNameContainingIgnoreCase("nguyen");
        log.info("Found {} users with name containing 'nguyen'", users.size());
        
        // 4. Tìm theo độ tuổi
        List<User> usersByAge = userRepository.findByAgeBetween(18, 30);
        log.info("Found {} users between 18-30", usersByAge.size());
        
        // 5. Phân trang
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<User> page = userRepository.findByStatus("ACTIVE", pageable);
        log.info("Active users page: total={}, size={}", page.getTotalElements(), page.getContent().size());
        
        // 6. Top 5 user mới nhất
        List<User> topUsers = userRepository.findTop5ByOrderByCreatedAtDesc();
        log.info("Top 5 newest users: {}", topUsers.size());
        
        // 7. Đếm user theo status
        long activeCount = userRepository.countByStatus("ACTIVE");
        log.info("Active users count: {}", activeCount);
    }
    
    // ========== TEST 2: JPQL QUERY ==========
    @Transactional(readOnly = true)
    public void testJPQLQuery() {
        log.info("=== TEST JPQL QUERY ===");
        
        // 1. Tìm theo email (JPQL)
        userRepository.findByEmailJPQL("test@example.com")
            .ifPresent(user -> log.info("JPQL - Found user: {}", user.getName()));
        
        // 2. Tìm kiếm theo keyword
        List<User> searchResults = userRepository.searchByKeyword("nguyen");
        log.info("JPQL - Search results: {}", searchResults.size());
        
        // 3. Tìm theo khoảng ngày
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();
        List<User> usersByDate = userRepository.findByCreatedDateRange(start, end);
        log.info("JPQL - Users created in last 7 days: {}", usersByDate.size());
        
        // 4. Tìm theo khoảng tuổi
        List<User> usersByAge = userRepository.findByAgeRange(18, 30);
        log.info("JPQL - Users between 18-30: {}", usersByAge.size());
        
        // 5. Tìm user active sắp xếp theo ngày tạo
        List<User> activeUsers = userRepository.findActiveUsersOrderByCreatedAt("ACTIVE");
        log.info("JPQL - Active users ordered: {}", activeUsers.size());
    }
    
    // ========== TEST 3: NATIVE SQL QUERY ==========
    @Transactional(readOnly = true)
    public void testNativeQuery() {
        log.info("=== TEST NATIVE SQL QUERY ===");
        
        // 1. Tìm theo email (Native SQL)
        userRepository.findByEmailNative("test@example.com")
            .ifPresent(user -> log.info("Native - Found user: {}", user.getName()));
        
        // 2. Tìm user theo tuổi (Native SQL)
        List<User> users = userRepository.findUsersByAgeNative(18);
        log.info("Native - Users age >= 18: {}", users.size());
    }
    
    // ========== TEST 4: UPDATE & DELETE QUERY ==========
    @Transactional
    public void testUpdateDeleteQuery() {
        log.info("=== TEST UPDATE & DELETE QUERY ===");
        
        // 1. Cập nhật status user
        int updated = userRepository.updateUserStatus(1L, "INACTIVE");
        log.info("Updated {} user(s) status to INACTIVE", updated);
        
        // 2. Cập nhật tên user
        int nameUpdated = userRepository.updateUserName(1L, "Updated Name");
        log.info("Updated {} user(s) name", nameUpdated);
        
        // 3. Xóa user inactive cũ
        LocalDateTime cutoffDate = LocalDateTime.now().minusMonths(6);
        int deleted = userRepository.deleteInactiveUsers("INACTIVE", cutoffDate);
        log.info("Deleted {} inactive users", deleted);
        
        // 4. Cập nhật status order
        int orderUpdated = orderRepository.updateOrderStatus(1L, "COMPLETED");
        log.info("Updated {} order(s) status", orderUpdated);
        
        // 5. Xóa order cũ
        int ordersDeleted = orderRepository.deleteOldCancelledOrders(cutoffDate);
        log.info("Deleted {} old cancelled orders", ordersDeleted);
    }
    
    // ========== TEST 5: ORDER QUERY ==========
    @Transactional(readOnly = true)
    public void testOrderQuery() {
        log.info("=== TEST ORDER QUERY ===");
        
        // 1. Tìm order theo user
        List<Order> userOrders = orderRepository.findByUserId(1L);
        log.info("User 1 has {} orders", userOrders.size());
        
        // 2. Tìm order theo status
        List<Order> pendingOrders = orderRepository.findByStatus("PENDING");
        log.info("Pending orders: {}", pendingOrders.size());
        
        // 3. Tìm order amount > 100
        List<Order> largeOrders = orderRepository.findByAmountGreaterThan(new BigDecimal("100"));
        log.info("Orders > 100: {}", largeOrders.size());
        
        // 4. Tìm order theo user và status
        List<Order> userCompleted = orderRepository.findByUserIdAndStatus(1L, "COMPLETED");
        log.info("User 1 completed orders: {}", userCompleted.size());
        
        // 5. Tổng tiền user đã chi
        BigDecimal totalSpent = orderRepository.getTotalSpentByUser(1L);
        log.info("User 1 total spent: {}", totalSpent);
        
        // 6. Đếm order theo status
        long completedCount = orderRepository.countByStatus("COMPLETED");
        log.info("Completed orders count: {}", completedCount);
        
        // 7. Order mới nhất của user
        Order latestOrder = orderRepository.findLatestOrderByUser(1L);
        if (latestOrder != null) {
            log.info("User 1 latest order: {}", latestOrder.getOrderNumber());
        }
    }
    
    // ========== TEST 6: TẠO DỮ LIỆU TEST ==========
    @Transactional
    public void createTestData() {
        log.info("=== CREATE TEST DATA ===");
        
        // Tạo user test
        User user = User.builder()
            .name("Nguyen Van A")
            .email("vana@example.com")
            .password("123456")
            .age(25)
            .status("ACTIVE")
            .build();
        
        User savedUser = userRepository.save(user);
        log.info("Created user: {}", savedUser.getId());
        
        // Tạo orders cho user
        for (int i = 1; i <= 3; i++) {
            Order order = Order.builder()
                .orderNumber("ORD00" + i)
                .amount(new BigDecimal(100 * i))
                .status(i == 1 ? "PENDING" : "COMPLETED")
                .user(savedUser)
                .build();
            orderRepository.save(order);
        }
        log.info("Created 3 orders for user");
    }
}