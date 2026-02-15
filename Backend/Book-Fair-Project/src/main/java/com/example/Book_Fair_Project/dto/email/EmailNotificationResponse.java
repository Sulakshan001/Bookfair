package com.example.Book_Fair_Project.dto.email;



import java.time.LocalDateTime;

public class EmailNotificationResponse {

    private Long emailId;
    private Long userId;
    private Long reservationId; // nullable
    private String emailType;
    private String emailStatus;
    private String subject;
    private LocalDateTime sentAt;

    public EmailNotificationResponse() {}

    public EmailNotificationResponse(Long emailId, Long userId, Long reservationId,
                                     String emailType, String emailStatus,
                                     String subject, LocalDateTime sentAt) {
        this.emailId = emailId;
        this.userId = userId;
        this.reservationId = reservationId;
        this.emailType = emailType;
        this.emailStatus = emailStatus;
        this.subject = subject;
        this.sentAt = sentAt;
    }

    public Long getEmailId() { return emailId; }
    public void setEmailId(Long emailId) { this.emailId = emailId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }

    public String getEmailType() { return emailType; }
    public void setEmailType(String emailType) { this.emailType = emailType; }

    public String getEmailStatus() { return emailStatus; }
    public void setEmailStatus(String emailStatus) { this.emailStatus = emailStatus; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
}
