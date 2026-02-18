package com.example.Book_Fair_Project.service;

import com.example.Book_Fair_Project.dto.email.EmailNotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {
    EmailNotificationResponse getNotificationById(Long emailId);
    List<EmailNotificationResponse> getUserNotifications(Long userId);
    List<EmailNotificationResponse> getReservationNotifications(Long reservationId);
    List<EmailNotificationResponse> getNotificationsByStatus(String status);
    Page<EmailNotificationResponse> getAllNotifications(Pageable pageable);
    EmailNotificationResponse deleteNotification(Long emailId);
    Long getTotalNotificationsCount();
    List<EmailNotificationResponse> getFailedNotifications();
    EmailNotificationResponse resendFailedNotification(Long emailId);
}
