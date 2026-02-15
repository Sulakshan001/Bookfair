package com.example.Book_Fair_Project.dto.otp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OtpVerifyRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String otpCode;

    @NotNull
    private String otpType;

    public OtpVerifyRequest() {}

    public OtpVerifyRequest(Long userId, String otpCode, String otpType) {
        this.userId = userId;
        this.otpCode = otpCode;
        this.otpType = otpType;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }

    public String getOtpType() { return otpType; }
    public void setOtpType(String otpType) { this.otpType = otpType; }
}

