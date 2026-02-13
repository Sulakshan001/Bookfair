package com.example.Book_Fair_Project.repository;


import com.example.Book_Fair_Project.model.EmailNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailNotificationRepository extends JpaRepository<EmailNotification, Long> {
    List<EmailNotification> findByUser_UserId(Long userId);
    List<EmailNotification> findByReservation_ReservationId(Long reservationId);
    List<EmailNotification> findByEmailStatus(EmailNotification.EmailStatus status);
}
