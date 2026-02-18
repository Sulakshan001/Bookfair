package com.example.Book_Fair_Project.controller;

import com.example.Book_Fair_Project.dto.common.ApiResponse;
import com.example.Book_Fair_Project.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/count")
@PreAuthorize("hasAnyAuthority('ADMIN')")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CountController {

    private final AdminService adminService;

    public CountController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Get all dashboard counts in a single call
     *
     * @return Combined counts for Users, Stalls, Reservations, etc.
     */
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardCounts() {
        try {
            Map<String, Object> counts = new HashMap<>();

            // User counts
            counts.put("totalUsers", adminService.getTotalUsersCount());

            // Stall counts
            counts.put("totalStalls", adminService.getTotalStallsCount());
            counts.put("availableStalls", adminService.getAvailableStallsCount());
            counts.put("reservedStalls", adminService.getReservedStallsCount());

            // Reservation counts
            counts.put("totalReservations", adminService.getTotalReservationsCount());

            // Payment counts
            counts.put("totalPayments", adminService.getTotalPaymentsCount());
            counts.put("successfulPayments", adminService.getSuccessfulPaymentsCount());
            counts.put("pendingPayments", adminService.getPendingPaymentsCount());
            counts.put("failedPayments", adminService.getFailedPaymentsCount());
            counts.put("totalRevenue", adminService.getTotalPaymentsAmount());

            return ResponseEntity.ok(
                    ApiResponse.ok("Dashboard counts retrieved successfully", counts, 200));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    ApiResponse.fail("Error retrieving dashboard counts: " + e.getMessage(), 500));
        }
    }

    /**
     * Get user counts
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Long>> getUserCount() {
        long count = adminService.getTotalUsersCount();
        return ResponseEntity.ok(ApiResponse.ok("Total users count", count, 200));
    }

    /**
     * Get stall counts
     */
    @GetMapping("/stalls")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getStallCounts() {
        Map<String, Long> stallCounts = new HashMap<>();
        stallCounts.put("total", adminService.getTotalStallsCount());
        stallCounts.put("available", adminService.getAvailableStallsCount());
        stallCounts.put("reserved", adminService.getReservedStallsCount());
        return ResponseEntity.ok(ApiResponse.ok("Stall counts retrieved successfully", stallCounts, 200));
    }

    /**
     * Get reservation counts
     */
    @GetMapping("/reservations")
    public ResponseEntity<ApiResponse<Long>> getReservationCount() {
        long count = adminService.getTotalReservationsCount();
        return ResponseEntity.ok(ApiResponse.ok("Total reservations count", count, 200));
    }

    /**
     * Get payment counts and statistics
     */
    @GetMapping("/payments")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPaymentCounts() {
        Map<String, Object> paymentCounts = new HashMap<>();
        paymentCounts.put("total", adminService.getTotalPaymentsCount());
        paymentCounts.put("successful", adminService.getSuccessfulPaymentsCount());
        paymentCounts.put("pending", adminService.getPendingPaymentsCount());
        paymentCounts.put("failed", adminService.getFailedPaymentsCount());
        paymentCounts.put("totalAmount", adminService.getTotalPaymentsAmount());
        return ResponseEntity.ok(ApiResponse.ok("Payment counts retrieved successfully", paymentCounts, 200));
    }
}