package com.example.Book_Fair_Project.dto.user;

import java.time.LocalDateTime;

public class UserResponse {

    private Long userId;
    private String name;
    private String email;
    private String businessName;
    private String role;
    private boolean emailVerified;
    private LocalDateTime createdAt;

    public UserResponse() {}

    public UserResponse(Long userId, String name, String email, String businessName,
                        String role, boolean emailVerified, LocalDateTime createdAt) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.businessName = businessName;
        this.role = role;
        this.emailVerified = emailVerified;
        this.createdAt = createdAt;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isEmailVerified() { return emailVerified; }
    public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

