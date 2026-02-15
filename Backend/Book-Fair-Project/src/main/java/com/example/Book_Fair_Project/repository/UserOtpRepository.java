package com.example.Book_Fair_Project.repository;

import com.example.Book_Fair_Project.model.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserOtpRepository extends JpaRepository<UserOtp, Long> {

    Optional<UserOtp> findTopByUser_UserIdAndOtpTypeAndUsedFalseOrderByCreatedAtDesc(
            Long userId, UserOtp.OtpType otpType
    );

    long countByUser_UserIdAndOtpTypeAndCreatedAtAfter(Long userId, UserOtp.OtpType otpType, LocalDateTime after);

}

