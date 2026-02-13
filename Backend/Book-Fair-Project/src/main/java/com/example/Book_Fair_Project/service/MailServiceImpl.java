package com.example.Book_Fair_Project.service;

import com.example.Book_Fair_Project.exception.BadRequestException;
import com.example.Book_Fair_Project.model.EmailNotification;
import com.example.Book_Fair_Project.model.Reservation;
import com.example.Book_Fair_Project.model.User;
import com.example.Book_Fair_Project.repository.EmailNotificationRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final EmailNotificationRepository emailNotificationRepository;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public MailServiceImpl(JavaMailSender mailSender,
                           EmailNotificationRepository emailNotificationRepository) {
        this.mailSender = mailSender;
        this.emailNotificationRepository = emailNotificationRepository;
    }

    // ===================== BASIC PLAIN TEXT =====================
    @Override
    public void sendMail(String to, String subject, String body) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromEmail);
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(body);
            mailSender.send(msg);
        } catch (MailException e) {
            throw new BadRequestException("Email sending failed: " + e.getMessage());
        }
    }

    // ===================== PLAIN TEXT + LOG =====================
    @Override
    public void sendAndLog(User user,
                           Reservation reservation,
                           EmailNotification.EmailType emailType,
                           String subject,
                           String body) {

        EmailNotification n = new EmailNotification();
        n.setUser(user);
        n.setReservation(reservation);
        n.setEmailType(emailType);
        n.setSubject(subject);

        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromEmail);
            msg.setTo(user.getEmail());
            msg.setSubject(subject);
            msg.setText(body);

            mailSender.send(msg);

            n.setEmailStatus(EmailNotification.EmailStatus.SENT);
            emailNotificationRepository.save(n);

        } catch (MailException e) {
            n.setEmailStatus(EmailNotification.EmailStatus.FAILED);
            emailNotificationRepository.save(n);

            // ✅ IMPORTANT: throw so OTP is NOT saved and frontend sees error
            throw new BadRequestException("Mail server connection failed: " + e.getMessage());
        }
    }

    // ===================== HTML + LOG =====================
    @Override
    public void sendAndLogHtml(User user,
                               Reservation reservation,
                               EmailNotification.EmailType emailType,
                               String subject,
                               String htmlBody) {

        EmailNotification n = new EmailNotification();
        n.setUser(user);
        n.setReservation(reservation);
        n.setEmailType(emailType);
        n.setSubject(subject);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(user.getEmail());
            helper.setSubject(subject);

            // ✅ HTML enabled
            helper.setText(htmlBody, true);

            mailSender.send(message);

            n.setEmailStatus(EmailNotification.EmailStatus.SENT);
            emailNotificationRepository.save(n);

        } catch (Exception e) {
            n.setEmailStatus(EmailNotification.EmailStatus.FAILED);
            emailNotificationRepository.save(n);

            // ✅ IMPORTANT: throw so OTP is NOT saved and frontend sees error
            throw new BadRequestException("Mail server connection failed: " + e.getMessage());
        }
    }
}
