package com.example.Book_Fair_Project.service;

import com.example.Book_Fair_Project.dto.stall.StallResponse;
import com.example.Book_Fair_Project.dto.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService {

    // ========== USER MANAGEMENT ==========
    Page<UserResponse> getAllUsers(Pageable pageable);

    List<UserResponse> getUsersByRole(String role);

    UserResponse getUserById(Long userId);

    UserResponse updateUserRole(Long userId, String newRole);

    void deleteUser(Long userId);

    long getTotalUsersCount();

    // ========== STALL MANAGEMENT ==========
    List<StallResponse> getAllStalls();

    StallResponse updateStallStatus(Long stallId, String status);

    List<StallResponse> getStallsByStatus(String status);

    void deleteStall(Long stallId);

    long getTotalStallsCount();

    long getAvailableStallsCount();

    long getReservedStallsCount();
}
