package com.example.Book_Fair_Project.controller;

import com.example.Book_Fair_Project.dto.common.ApiResponse;
import com.example.Book_Fair_Project.dto.email.EmailNotificationResponse;
import com.example.Book_Fair_Project.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")

public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // ✅ Get notification by ID
    @GetMapping("/{emailId}")
    public ResponseEntity<ApiResponse<EmailNotificationResponse>> getNotificationById(@PathVariable Long emailId) {
        EmailNotificationResponse notification = notificationService.getNotificationById(emailId);
        return ResponseEntity.ok(ApiResponse.ok("Notification retrieved successfully", notification, 200));
    }

    // ✅ Get user notifications
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<EmailNotificationResponse>>> getUserNotifications(@PathVariable Long userId) {
        List<EmailNotificationResponse> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(ApiResponse.ok("User notifications retrieved successfully", notifications, 200));
    }

    // ✅ Get reservation notifications
    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<ApiResponse<List<EmailNotificationResponse>>> getReservationNotifications(@PathVariable Long reservationId) {
        List<EmailNotificationResponse> notifications = notificationService.getReservationNotifications(reservationId);
        return ResponseEntity.ok(ApiResponse.ok("Reservation notifications retrieved successfully", notifications, 200));
    }

    // ✅ Get notifications by status
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<EmailNotificationResponse>>> getNotificationsByStatus(@PathVariable String status) {
        List<EmailNotificationResponse> notifications = notificationService.getNotificationsByStatus(status);
        return ResponseEntity.ok(ApiResponse.ok("Notifications with status " + status + " retrieved successfully", notifications, 200));
    }

    // ✅ Get all notifications (paginated)
    @GetMapping
    public ResponseEntity<ApiResponse<Page<EmailNotificationResponse>>> getAllNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EmailNotificationResponse> notifications = notificationService.getAllNotifications(pageable);
        return ResponseEntity.ok(ApiResponse.ok("All notifications retrieved successfully", notifications, 200));
    }

    // ✅ Get failed notifications
    @GetMapping("/failed")
    public ResponseEntity<ApiResponse<List<EmailNotificationResponse>>> getFailedNotifications() {
        List<EmailNotificationResponse> notifications = notificationService.getFailedNotifications();
        return ResponseEntity.ok(ApiResponse.ok("Failed notifications retrieved successfully", notifications, 200));
    }

    // ✅ Get total notifications count
    @GetMapping("/count/total")
    public ResponseEntity<ApiResponse<Long>> getTotalNotificationsCount() {
        Long count = notificationService.getTotalNotificationsCount();
        return ResponseEntity.ok(ApiResponse.ok("Total notifications count", count, 200));
    }

    // ✅ Delete notification
    @DeleteMapping("/{emailId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<EmailNotificationResponse>> deleteNotification(@PathVariable Long emailId) {
        EmailNotificationResponse notification = notificationService.deleteNotification(emailId);
        return ResponseEntity.ok(ApiResponse.ok("Notification deleted successfully", notification, 200));
    }

    // ✅ Resend failed notification
    @PostMapping("/{emailId}/resend")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<EmailNotificationResponse>> resendFailedNotification(@PathVariable Long emailId) {
        EmailNotificationResponse notification = notificationService.resendFailedNotification(emailId);
        return ResponseEntity.ok(ApiResponse.ok("Notification resent successfully", notification, 200));
    }
}
