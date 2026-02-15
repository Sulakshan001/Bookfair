package com.example.Book_Fair_Project.dto.otp;

import jakarta.validation.constraints.NotNull;


public class OtpCreateRequest {

    @NotNull
    private Long userId;

    @NotNull
    private String otpType; // EMAIL_VERIFY / PASSWORD_RESET

    public OtpCreateRequest() {}

    public OtpCreateRequest(Long userId, String otpType) {
        this.userId = userId;
        this.otpType = otpType;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getOtpType() { return otpType; }
    public void setOtpType(String otpType) { this.otpType = otpType; }
}

