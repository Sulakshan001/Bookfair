package com.example.Book_Fair_Project.service;

import com.example.Book_Fair_Project.dto.email.EmailNotificationResponse;
import com.example.Book_Fair_Project.dto.payment.PaymentResponse;
import com.example.Book_Fair_Project.dto.qr.QrPassResponse;
import com.example.Book_Fair_Project.dto.reservation.ReservationResponse;
import com.example.Book_Fair_Project.dto.stall.StallResponse;
import com.example.Book_Fair_Project.dto.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

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


    // ========== RESERVATION MANAGEMENT ==========
    Page<ReservationResponse> getAllReservations(Pageable pageable);

    ReservationResponse getReservationById(Long reservationId);

    void cancelReservation(Long reservationId);

    void sendReservationConfirmationEmail(Long reservationId);

    long getTotalReservationsCount();

    // ========== PAYMENT MANAGEMENT ==========
    Page<PaymentResponse> getAllPayments(Pageable pageable);

    PaymentResponse getPaymentById(Long paymentId);

    List<PaymentResponse> getPaymentsByStatus(String status);

    long getTotalPaymentsAmount();

    long getTotalPaymentsCount();

    long getSuccessfulPaymentsCount();

    long getPendingPaymentsCount();

    long getFailedPaymentsCount();

    PaymentResponse updatePaymentStatus(Long paymentId, String status);

    PaymentResponse confirmPayment(Long paymentId);

    void sendPaymentConfirmationEmail(Long paymentId);

    // ========== EMAIL NOTIFICATIONS ==========
    Page<EmailNotificationResponse> getAllEmailNotifications(Pageable pageable);

    void resendEmailNotification(Long emailNotificationId);

    List<EmailNotificationResponse> getFailedEmailNotifications();


    // ========== QR PASS MANAGEMENT ==========
    Page<QrPassResponse> getAllQrPasses(Pageable pageable);

    QrPassResponse getQrPassById(Long qrId);

    QrPassResponse getQrPassByReservationId(Long reservationId);

    QrPassResponse getQrPassByQrCode(String qrCode);

    QrPassResponse activateQrPass(Long qrId);

    QrPassResponse deactivateQrPass(Long qrId);

    QrPassResponse markQrPassAsUsed(Long qrId);

    void deleteQrPass(Long qrId);

    long getTotalQrPassesCount();

    long getActiveQrPassesCount();

    long getInactiveQrPassesCount();


    // ========== DASHBOARD STATISTICS ==========
    Map<String, Object> getDashboardStatistics();

    Map<String, Object> getSystemMetrics();


}
