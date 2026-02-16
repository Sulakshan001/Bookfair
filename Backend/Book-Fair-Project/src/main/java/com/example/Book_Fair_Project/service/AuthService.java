package com.example.Book_Fair_Project.service;

import com.example.Book_Fair_Project.dto.auth.*;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);

    void forgotPassword(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);

    void verifyEmail(VerifyEmailRequest request);
    void resendVerifyOtp(String email);

    // Utility method for testing - send OTP to any unverified email
    void sendOtpToEmail(String email);
}
