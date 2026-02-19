package com.example.Book_Fair_Project.controller;

import com.example.Book_Fair_Project.dto.auth.*;
import com.example.Book_Fair_Project.dto.common.ApiResponse;
import com.example.Book_Fair_Project.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ✅ 1) REGISTER (USER / VENDOR / PUBLISHER)
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse res = authService.register(request);
        return ResponseEntity.status(201)
                .body(ApiResponse.ok("Registered successfully. OTP sent to email.", res, 201));
    }

    // ✅ 2) VERIFY EMAIL OTP
    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        authService.verifyEmail(request);
        return ResponseEntity.ok(ApiResponse.ok("Email verified successfully", null, 200));
    }

    // ✅ 3) RESEND VERIFY OTP
    @PostMapping("/resend-verify-otp")
    public ResponseEntity<ApiResponse<Void>> resendVerifyOtp(@RequestParam String email) {
        authService.resendVerifyOtp(email);
        return ResponseEntity.ok(ApiResponse.ok("Verification OTP sent", null, 200));
    }

    // ✅ 4) LOGIN
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse res = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Login successful", res, 200));
    }

    // ✅ 5) FORGOT PASSWORD (send OTP)
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok(ApiResponse.ok("OTP sent to email", null, 200));
    }

    // ✅ 6) RESET PASSWORD (verify OTP + set new password)
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.ok("Password reset successful", null, 200));
    }

    // ✅ 7) SEND OTP EMAIL (Manual - for testing)
    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<Void>> sendOtp(@RequestParam String email) {
        authService.sendOtpToEmail(email);
        return ResponseEntity.ok(ApiResponse.ok("OTP sent to email", null, 200));
    }
}