package com.example.Book_Fair_Project.model;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_notification")
public class EmailNotification {

    public enum EmailType {
        RESERVATION_CONFIRMATION, QR_PASS, GENERAL
    }

    public enum EmailStatus {
        SENT, FAILED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_id")
    private Long emailId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // reservation_id is NULLABLE in your ER :contentReference[oaicite:11]{index=11}
    @ManyToOne(optional = true)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Enumerated(EnumType.STRING)
    @Column(name = "email_type", nullable = false)
    private EmailType emailType;

    @Enumerated(EnumType.STRING)
    @Column(name = "email_status", nullable = false)
    private EmailStatus emailStatus;

    @Column(length = 150)
    private String subject;

    @CreationTimestamp
    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    public EmailNotification() {}

    public EmailNotification(Long emailId, User user, Reservation reservation, EmailType emailType,
                             EmailStatus emailStatus, String subject, LocalDateTime sentAt) {
        this.emailId = emailId;
        this.user = user;
        this.reservation = reservation;
        this.emailType = emailType;
        this.emailStatus = emailStatus;
        this.subject = subject;
        this.sentAt = sentAt;
    }

    public Long getEmailId() { return emailId; }
    public void setEmailId(Long emailId) { this.emailId = emailId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Reservation getReservation() { return reservation; }
    public void setReservation(Reservation reservation) { this.reservation = reservation; }

    public EmailType getEmailType() { return emailType; }
    public void setEmailType(EmailType emailType) { this.emailType = emailType; }

    public EmailStatus getEmailStatus() { return emailStatus; }
    public void setEmailStatus(EmailStatus emailStatus) { this.emailStatus = emailStatus; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
}

