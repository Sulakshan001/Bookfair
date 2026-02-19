package com.example.Book_Fair_Project.controller;


import com.example.Book_Fair_Project.dto.common.ApiResponse;
import com.example.Book_Fair_Project.dto.email.EmailNotificationResponse;
import com.example.Book_Fair_Project.dto.payment.PaymentResponse;
import com.example.Book_Fair_Project.dto.qr.QrPassResponse;
import com.example.Book_Fair_Project.dto.reservation.ReservationResponse;
import com.example.Book_Fair_Project.dto.stall.StallResponse;
import com.example.Book_Fair_Project.dto.user.UserResponse;
import com.example.Book_Fair_Project.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ========== USER MANAGEMENT ENDPOINTS ==========

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponse> users = adminService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.ok("Users retrieved successfully", users, 200));
    }

    @GetMapping("/users/role/{role}")
    public ResponseEntity<ApiResponse<Object>> getUsersByRole(@PathVariable String role) {
        var users = adminService.getUsersByRole(role);
        return ResponseEntity.ok(ApiResponse.ok("Users with role " + role + " retrieved successfully", users, 200));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long userId) {
        UserResponse user = adminService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.ok("User retrieved successfully", user, 200));
    }

    @PutMapping("/users/{userId}/role/{newRole}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserRole(
            @PathVariable Long userId,
            @PathVariable String newRole) {
        UserResponse user = adminService.updateUserRole(userId, newRole);
        return ResponseEntity.ok(ApiResponse.ok("User role updated successfully", user, 200));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.ok("User deleted successfully", null, 200));
    }

    @GetMapping("/users/count")
    public ResponseEntity<ApiResponse<Long>> getTotalUsersCount() {
        long count = adminService.getTotalUsersCount();
        return ResponseEntity.ok(ApiResponse.ok("Total users count", count, 200));
    }

    // ========== STALL MANAGEMENT ENDPOINTS ==========

    @GetMapping("/stalls")
    public ResponseEntity<ApiResponse<Object>> getAllStalls() {
        var stalls = adminService.getAllStalls();
        return ResponseEntity.ok(ApiResponse.ok("All stalls retrieved successfully", stalls, 200));


    }

    @PostMapping("/stalls")
    public ResponseEntity<ApiResponse<StallResponse>> createStall(@RequestBody StallResponse stallRequest) {
        StallResponse createdStall = adminService.createStall(stallRequest);
        return ResponseEntity.ok(ApiResponse.ok("Stall created successfully", createdStall, 201));
    }

    @PutMapping("/stalls/{stallId}/status/{status}")
    public ResponseEntity<ApiResponse<StallResponse>> updateStallStatus(
            @PathVariable Long stallId,
            @PathVariable String status) {
        StallResponse stall = adminService.updateStallStatus(stallId, status);
        return ResponseEntity.ok(ApiResponse.ok("Stall status updated successfully", stall, 200));
    }

    @GetMapping("/stalls/status/{status}")
    public ResponseEntity<ApiResponse<Object>> getStallsByStatus(@PathVariable String status) {
        var stalls = adminService.getStallsByStatus(status);
        return ResponseEntity.ok(ApiResponse.ok("Stalls with status " + status + " retrieved successfully", stalls, 200));
    }

    @DeleteMapping("/stalls/{stallId}")
    public ResponseEntity<ApiResponse<Void>> deleteStall(@PathVariable Long stallId) {
        adminService.deleteStall(stallId);
        return ResponseEntity.ok(ApiResponse.ok("Stall deleted successfully", null, 200));
    }

    @GetMapping("/stalls/count/total")
    public ResponseEntity<ApiResponse<Long>> getTotalStallsCount() {
        long count = adminService.getTotalStallsCount();
        return ResponseEntity.ok(ApiResponse.ok("Total stalls count", count, 200));
    }

    @GetMapping("/stalls/count/available")
    public ResponseEntity<ApiResponse<Long>> getAvailableStallsCount() {
        long count = adminService.getAvailableStallsCount();
        return ResponseEntity.ok(ApiResponse.ok("Available stalls count", count, 200));
    }

    @GetMapping("/stalls/count/reserved")
    public ResponseEntity<ApiResponse<Long>> getReservedStallsCount() {
        long count = adminService.getReservedStallsCount();
        return ResponseEntity.ok(ApiResponse.ok("Reserved stalls count", count, 200));
    }

    // ========== RESERVATION MANAGEMENT ENDPOINTS ==========

    @GetMapping("/reservations")
    public ResponseEntity<ApiResponse<Page<ReservationResponse>>> getAllReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservationResponse> reservations = adminService.getAllReservations(pageable);
        return ResponseEntity.ok(ApiResponse.ok("All reservations retrieved successfully", reservations, 200));
    }

    @GetMapping("/reservations/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationResponse>> getReservationById(@PathVariable Long reservationId) {
        ReservationResponse reservation = adminService.getReservationById(reservationId);
        return ResponseEntity.ok(ApiResponse.ok("Reservation retrieved successfully", reservation, 200));
    }

    @DeleteMapping("/reservations/{reservationId}")
    public ResponseEntity<ApiResponse<Void>> cancelReservation(@PathVariable Long reservationId) {
        adminService.cancelReservation(reservationId);
        return ResponseEntity.ok(ApiResponse.ok("Reservation cancelled successfully", null, 200));
    }

    @PostMapping("/reservations/{reservationId}/send-confirmation-email")
    public ResponseEntity<ApiResponse<Void>> sendReservationConfirmationEmail(@PathVariable Long reservationId) {
        adminService.sendReservationConfirmationEmail(reservationId);
        return ResponseEntity.ok(ApiResponse.ok("Reservation confirmation email sent successfully", null, 200));
    }

    @GetMapping("/reservations/count")
    public ResponseEntity<ApiResponse<Long>> getTotalReservationsCount() {
        long count = adminService.getTotalReservationsCount();
        return ResponseEntity.ok(ApiResponse.ok("Total reservations count", count, 200));
    }

    // ========== PAYMENT MANAGEMENT ENDPOINTS ==========

    @GetMapping("/payments")
    public ResponseEntity<ApiResponse<Page<PaymentResponse>>> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PaymentResponse> payments = adminService.getAllPayments(pageable);
        return ResponseEntity.ok(ApiResponse.ok("All payments retrieved successfully", payments, 200));
    }

    @GetMapping("/payments/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentById(@PathVariable Long paymentId) {
        PaymentResponse payment = adminService.getPaymentById(paymentId);
        return ResponseEntity.ok(ApiResponse.ok("Payment retrieved successfully", payment, 200));
    }

    @GetMapping("/payments/status/{status}")
    public ResponseEntity<ApiResponse<Object>> getPaymentsByStatus(@PathVariable String status) {
        var payments = adminService.getPaymentsByStatus(status);
        return ResponseEntity.ok(ApiResponse.ok("Payments with status " + status + " retrieved successfully", payments, 200));
    }

    @GetMapping("/payments/count/successful")
    public ResponseEntity<ApiResponse<Long>> getSuccessfulPaymentsCount() {
        long count = adminService.getSuccessfulPaymentsCount();
        return ResponseEntity.ok(ApiResponse.ok("Successful payments count", count, 200));
    }

    @GetMapping("/payments/count/pending")
    public ResponseEntity<ApiResponse<Long>> getPendingPaymentsCount() {
        long count = adminService.getPendingPaymentsCount();
        return ResponseEntity.ok(ApiResponse.ok("Pending payments count", count, 200));
    }

    @GetMapping("/payments/count/failed")
    public ResponseEntity<ApiResponse<Long>> getFailedPaymentsCount() {
        long count = adminService.getFailedPaymentsCount();
        return ResponseEntity.ok(ApiResponse.ok("Failed payments count", count, 200));
    }

    @GetMapping("/payments/total-amount")
    public ResponseEntity<ApiResponse<Long>> getTotalPaymentsAmount() {
        long total = adminService.getTotalPaymentsAmount();
        return ResponseEntity.ok(ApiResponse.ok("Total payments amount", total, 200));
    }

    @PutMapping("/payments/{paymentId}/status/{status}")
    public ResponseEntity<ApiResponse<PaymentResponse>> updatePaymentStatus(
            @PathVariable Long paymentId,
            @PathVariable String status) {
        PaymentResponse payment = adminService.updatePaymentStatus(paymentId, status);
        return ResponseEntity.ok(ApiResponse.ok("Payment status updated successfully", payment, 200));
    }

    @PutMapping("/payments/{paymentId}/confirm")
    public ResponseEntity<ApiResponse<PaymentResponse>> confirmPayment(@PathVariable Long paymentId) {
        PaymentResponse payment = adminService.confirmPayment(paymentId);
        return ResponseEntity.ok(ApiResponse.ok("Payment confirmed successfully", payment, 200));
    }

    @PostMapping("/payments/{paymentId}/send-confirmation-email")
    public ResponseEntity<ApiResponse<Void>> sendPaymentConfirmationEmail(@PathVariable Long paymentId) {
        adminService.sendPaymentConfirmationEmail(paymentId);
        return ResponseEntity.ok(ApiResponse.ok("Payment confirmation email sent successfully", null, 200));
    }

    // ========== EMAIL NOTIFICATION ENDPOINTS ==========

    @GetMapping("/emails")
    public ResponseEntity<ApiResponse<Page<EmailNotificationResponse>>> getAllEmailNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EmailNotificationResponse> notifications = adminService.getAllEmailNotifications(pageable);
        return ResponseEntity.ok(ApiResponse.ok("All email notifications retrieved successfully", notifications, 200));
    }

    @PostMapping("/emails/{emailNotificationId}/resend")
    public ResponseEntity<ApiResponse<Void>> resendEmailNotification(@PathVariable Long emailNotificationId) {
        adminService.resendEmailNotification(emailNotificationId);
        return ResponseEntity.ok(ApiResponse.ok("Email notification resent successfully", null, 200));
    }

    @GetMapping("/emails/failed")
    public ResponseEntity<ApiResponse<Object>> getFailedEmailNotifications() {
        var notifications = adminService.getFailedEmailNotifications();
        return ResponseEntity.ok(ApiResponse.ok("Failed email notifications retrieved successfully", notifications, 200));
    }

    // ========== DASHBOARD ENDPOINTS ==========

    @GetMapping("/dashboard/statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> statistics = adminService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.ok("Dashboard statistics retrieved successfully", statistics, 200));
    }

    @GetMapping("/dashboard/metrics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSystemMetrics() {
        Map<String, Object> metrics = adminService.getSystemMetrics();
        return ResponseEntity.ok(ApiResponse.ok("System metrics retrieved successfully", metrics, 200));
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.ok("Admin panel is healthy", "ONLINE", 200));



    }

    // ========== QR PASS MANAGEMENT ENDPOINTS ==========

    @GetMapping("/qr-passes")
    public ResponseEntity<ApiResponse<Page<QrPassResponse>>> getAllQrPasses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "qrId") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));
        Page<QrPassResponse> qrPasses = adminService.getAllQrPasses(pageable);
        return ResponseEntity.ok(ApiResponse.ok("QR passes retrieved successfully", qrPasses, 200));
    }

    @GetMapping("/qr-passes/{qrId}")
    public ResponseEntity<ApiResponse<QrPassResponse>> getQrPassById(@PathVariable Long qrId) {
        QrPassResponse qr = adminService.getQrPassById(qrId);
        return ResponseEntity.ok(ApiResponse.ok("QR pass retrieved successfully", qr, 200));
    }

    @GetMapping("/qr-passes/by-reservation/{reservationId}")
    public ResponseEntity<ApiResponse<QrPassResponse>> getQrPassByReservationId(@PathVariable Long reservationId) {
        QrPassResponse qr = adminService.getQrPassByReservationId(reservationId);
        return ResponseEntity.ok(ApiResponse.ok("QR pass retrieved successfully", qr, 200));
    }

    @GetMapping("/qr-passes/by-code/{qrCode}")
    public ResponseEntity<ApiResponse<QrPassResponse>> getQrPassByQrCode(@PathVariable String qrCode) {
        QrPassResponse qr = adminService.getQrPassByQrCode(qrCode);
        return ResponseEntity.ok(ApiResponse.ok("QR pass retrieved successfully", qr, 200));
    }

    @PutMapping("/qr-passes/{qrId}/activate")
    public ResponseEntity<ApiResponse<QrPassResponse>> activateQrPass(@PathVariable Long qrId) {
        QrPassResponse qr = adminService.activateQrPass(qrId);
        return ResponseEntity.ok(ApiResponse.ok("QR pass activated successfully", qr, 200));
    }

    @PutMapping("/qr-passes/{qrId}/deactivate")
    public ResponseEntity<ApiResponse<QrPassResponse>> deactivateQrPass(@PathVariable Long qrId) {
        QrPassResponse qr = adminService.deactivateQrPass(qrId);
        return ResponseEntity.ok(ApiResponse.ok("QR pass deactivated successfully", qr, 200));
    }

    @PutMapping("/qr-passes/{qrId}/mark-used")
    public ResponseEntity<ApiResponse<QrPassResponse>> markQrPassAsUsed(@PathVariable Long qrId) {
        QrPassResponse qr = adminService.markQrPassAsUsed(qrId);
        return ResponseEntity.ok(ApiResponse.ok("QR pass marked as used successfully", qr, 200));
    }

    @DeleteMapping("/qr-passes/{qrId}")
    public ResponseEntity<ApiResponse<Void>> deleteQrPass(@PathVariable Long qrId) {
        adminService.deleteQrPass(qrId);
        return ResponseEntity.ok(ApiResponse.ok("QR pass deleted successfully", null, 200));
    }

    @GetMapping("/qr-passes/count/total")
    public ResponseEntity<ApiResponse<Long>> getTotalQrPassesCount() {
        long count = adminService.getTotalQrPassesCount();
        return ResponseEntity.ok(ApiResponse.ok("Total QR passes count", count, 200));
    }

    @GetMapping("/qr-passes/count/active")
    public ResponseEntity<ApiResponse<Long>> getActiveQrPassesCount() {
        long count = adminService.getActiveQrPassesCount();
        return ResponseEntity.ok(ApiResponse.ok("Active QR passes count", count, 200));
    }

    @GetMapping("/qr-passes/count/inactive")
    public ResponseEntity<ApiResponse<Long>> getInactiveQrPassesCount() {
        long count = adminService.getInactiveQrPassesCount();
        return ResponseEntity.ok(ApiResponse.ok("Inactive QR passes count", count, 200));
    }

}
