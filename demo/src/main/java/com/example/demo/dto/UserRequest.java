package com.example.demo.dto;
import com.example.demo.entity.User;
import com.example.demo.dto.group.CreateGroup;
import com.example.demo.dto.group.PatchGroup;
import com.example.demo.dto.group.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    
    // ========== 1. ID VALIDATION ==========
    // Chỉ validate khi update hoặc patch
    @NotNull(message = "ID không được để trống", groups = {UpdateGroup.class, PatchGroup.class})
    private Long id;
    
    // ========== 2. NAME VALIDATION ==========
    @NotBlank(message = "Tên không được để trống", groups = {CreateGroup.class, UpdateGroup.class})
    @Size(min = 2, max = 100, message = "Tên phải từ 2-100 ký tự", groups = {CreateGroup.class, UpdateGroup.class})
    @Pattern(
        regexp = "^[\\p{L}\\s]+$",
        message = "Tên chỉ được chứa chữ cái và khoảng trắng",
        groups = {CreateGroup.class, UpdateGroup.class}
    )
    private String name;
    
    // ========== 3. EMAIL VALIDATION ==========
    @NotBlank(message = "Email không được để trống", groups = {CreateGroup.class, UpdateGroup.class})
    @Email(message = "Email không đúng định dạng", groups = {CreateGroup.class, UpdateGroup.class})
    @Pattern(
        regexp = "^[A-Za-z0-9+_.-]+@(.+)$",
        message = "Email không hợp lệ",
        groups = {CreateGroup.class, UpdateGroup.class}
    )
    private String email;
    
    // ========== 4. PASSWORD VALIDATION ==========
    // Chỉ validate khi tạo mới
    @NotBlank(message = "Mật khẩu không được để trống", groups = {CreateGroup.class})
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự", groups = {CreateGroup.class})
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$",
        message = "Mật khẩu phải có ít nhất 1 chữ hoa, 1 số, 1 ký tự đặc biệt",
        groups = {CreateGroup.class}
    )
    private String password;
    
    // ========== 5. AGE VALIDATION ==========
    @Min(value = 18, message = "Phải từ 18 tuổi trở lên", groups = {CreateGroup.class, UpdateGroup.class})
    @Max(value = 100, message = "Tuổi không hợp lệ", groups = {CreateGroup.class, UpdateGroup.class})
    private Integer age;
    
    // ========== 6. PHONE NUMBER VALIDATION ==========
    @Pattern(
        regexp = "^(0|\\+84)\\d{9}$",
        message = "Số điện thoại phải bắt đầu bằng 0 hoặc +84 và có 10 số",
        groups = {CreateGroup.class, UpdateGroup.class}
    )
    private String phoneNumber;
    
    // ========== 7. BIRTH DATE VALIDATION ==========
    @Past(message = "Ngày sinh không được trong tương lai", groups = {CreateGroup.class, UpdateGroup.class})
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
    
    // ========== 8. STATUS VALIDATION ==========
    @Pattern(
        regexp = "^(ACTIVE|INACTIVE|BANNED)$",
        message = "Status phải là ACTIVE, INACTIVE hoặc BANNED",
        groups = {UpdateGroup.class, PatchGroup.class}
    )
    private String status;
    
    // ========== HELPER METHODS ==========
    
    // Chuyển từ Entity sang DTO
    public static UserRequest fromEntity(User user) {
        return UserRequest.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .age(user.getAge())
            .phoneNumber(user.getPhoneNumber())
            .birthDate(user.getBirthDate())
            .status(user.getStatus())
            .build();
        // Không lấy password ra
    }
    
    // Chuyển từ DTO sang Entity
    public User toEntity() {
        return User.builder()
            .id(this.id)
            .name(this.name)
            .email(this.email)
            .password(this.password)
            .age(this.age)
            .phoneNumber(this.phoneNumber)
            .birthDate(this.birthDate)
            .status(this.status != null ? this.status : "ACTIVE")
            .build();
    }
}