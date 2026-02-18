package com.example.Book_Fair_Project.service;

import com.example.Book_Fair_Project.model.EmailNotification;

import com.example.Book_Fair_Project.model.Reservation;
import com.example.Book_Fair_Project.model.User;

import java.util.Map;

public interface MailService {
    void sendMail(String to, String subject, String body);

    void sendAndLog(User user,
                    Reservation reservation,
                    EmailNotification.EmailType emailType,
                    String subject,
                    String body);

    void sendAndLogHtml(User user,
                        Reservation reservation,
                        EmailNotification.EmailType emailType,
                        String subject,
                        String htmlBody);

    // ✅ NEW: HTML + inline images (cid)
    void sendAndLogHtmlInline(User user,
                              Reservation reservation,
                              EmailNotification.EmailType emailType,
                              String subject,
                              String htmlBody,
                              Map<String, byte[]> inlinePngImages);





}
