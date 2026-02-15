package com.example.Book_Fair_Project.dto.otp;

import java.time.LocalDateTime;

public class UserOtpResponse {

    private Long otpId;
    private Long userId;
    private String otpCode;
    private String otpType;
    private LocalDateTime expiresAt;
    private boolean used;
    private LocalDateTime createdAt;

    public UserOtpResponse() {}

    public UserOtpResponse(Long otpId, Long userId, String otpCode, String otpType,
                           LocalDateTime expiresAt, boolean used, LocalDateTime createdAt) {
        this.otpId = otpId;
        this.userId = userId;
        this.otpCode = otpCode;
        this.otpType = otpType;
        this.expiresAt = expiresAt;
        this.used = used;
        this.createdAt = createdAt;
    }

    public Long getOtpId() { return otpId; }
    public void setOtpId(Long otpId) { this.otpId = otpId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }

    public String getOtpType() { return otpType; }
    public void setOtpType(String otpType) { this.otpType = otpType; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

