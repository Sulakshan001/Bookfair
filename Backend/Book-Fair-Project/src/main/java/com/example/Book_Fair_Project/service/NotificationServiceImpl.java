package com.example.Book_Fair_Project.service;

import com.example.Book_Fair_Project.dto.email.EmailNotificationResponse;
import com.example.Book_Fair_Project.exception.NotFoundException;
import com.example.Book_Fair_Project.mapper.DtoMapper;
import com.example.Book_Fair_Project.model.EmailNotification;
import com.example.Book_Fair_Project.repository.EmailNotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final EmailNotificationRepository emailNotificationRepository;
    private final MailService mailService;

    public NotificationServiceImpl(
            EmailNotificationRepository emailNotificationRepository,
            MailService mailService
    ) {
        this.emailNotificationRepository = emailNotificationRepository;
        this.mailService = mailService;
    }

    @Override
    @Transactional(readOnly = true)
    public EmailNotificationResponse getNotificationById(Long emailId) {
        EmailNotification notification = emailNotificationRepository.findById(emailId)
                .orElseThrow(() -> new NotFoundException("Notification not found: " + emailId));
        return DtoMapper.toEmailNotificationResponse(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailNotificationResponse> getUserNotifications(Long userId) {
        return emailNotificationRepository.findByUser_UserId(userId).stream()
                .map(DtoMapper::toEmailNotificationResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailNotificationResponse> getReservationNotifications(Long reservationId) {
        return emailNotificationRepository.findByReservation_ReservationId(reservationId).stream()
                .map(DtoMapper::toEmailNotificationResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailNotificationResponse> getNotificationsByStatus(String status) {
        try {
            EmailNotification.EmailStatus emailStatus = EmailNotification.EmailStatus.valueOf(status.toUpperCase());
            return emailNotificationRepository.findByEmailStatus(emailStatus).stream()
                    .map(DtoMapper::toEmailNotificationResponse)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid email status: " + status);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmailNotificationResponse> getAllNotifications(Pageable pageable) {
        return emailNotificationRepository.findAll(pageable)
                .map(DtoMapper::toEmailNotificationResponse);
    }

    @Override
    public EmailNotificationResponse deleteNotification(Long emailId) {
        EmailNotification notification = emailNotificationRepository.findById(emailId)
                .orElseThrow(() -> new NotFoundException("Notification not found: " + emailId));

        EmailNotificationResponse response = DtoMapper.toEmailNotificationResponse(notification);
        emailNotificationRepository.deleteById(emailId);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTotalNotificationsCount() {
        return emailNotificationRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailNotificationResponse> getFailedNotifications() {
        return emailNotificationRepository.findByEmailStatus(EmailNotification.EmailStatus.FAILED).stream()
                .map(DtoMapper::toEmailNotificationResponse)
                .toList();
    }

    @Override
    public EmailNotificationResponse resendFailedNotification(Long emailId) {
        EmailNotification notification = emailNotificationRepository.findById(emailId)
                .orElseThrow(() -> new NotFoundException("Notification not found: " + emailId));

        if (!notification.getEmailStatus().equals(EmailNotification.EmailStatus.FAILED)) {
            throw new IllegalArgumentException("Notification must have FAILED status to resend");
        }

        try {
            // Resend the email using MailService
            mailService.sendMail(
                    notification.getUser().getEmail(),
                    notification.getSubject(),
                    "Please find your booking details below."
            );

            notification.setEmailStatus(EmailNotification.EmailStatus.SENT);
            EmailNotification savedNotification = emailNotificationRepository.save(notification);
            return DtoMapper.toEmailNotificationResponse(savedNotification);
        } catch (Exception e) {
            notification.setEmailStatus(EmailNotification.EmailStatus.FAILED);
            emailNotificationRepository.save(notification);
            throw new RuntimeException("Failed to resend notification: " + e.getMessage());
        }
    }
}
