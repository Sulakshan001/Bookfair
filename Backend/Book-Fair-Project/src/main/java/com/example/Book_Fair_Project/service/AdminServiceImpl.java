package com.example.Book_Fair_Project.service;





import com.example.Book_Fair_Project.dto.email.EmailNotificationResponse;
import com.example.Book_Fair_Project.dto.payment.PaymentResponse;
import com.example.Book_Fair_Project.dto.qr.QrPassResponse;
import com.example.Book_Fair_Project.dto.reservation.ReservationResponse;
import com.example.Book_Fair_Project.dto.stall.StallResponse;
import com.example.Book_Fair_Project.dto.user.UserResponse;
import com.example.Book_Fair_Project.exception.NotFoundException;
import com.example.Book_Fair_Project.mapper.DtoMapper;
import com.example.Book_Fair_Project.model.*;
import com.example.Book_Fair_Project.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final StallRepository stallRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final EmailNotificationRepository emailNotificationRepository;
    private final ReservationStallRepository reservationStallRepository;
    private final QrPassRepository qrPassRepository;
    private final MailService mailService;

    public AdminServiceImpl(
            UserRepository userRepository,
            StallRepository stallRepository,
            ReservationRepository reservationRepository,
            PaymentRepository paymentRepository,
            EmailNotificationRepository emailNotificationRepository,
            ReservationStallRepository reservationStallRepository,
            QrPassRepository qrPassRepository,
            MailService mailService
    ) {
        this.userRepository = userRepository;
        this.stallRepository = stallRepository;
        this.reservationRepository = reservationRepository;
        this.paymentRepository = paymentRepository;
        this.emailNotificationRepository = emailNotificationRepository;
        this.reservationStallRepository = reservationStallRepository;
        this.qrPassRepository = qrPassRepository;
        this.mailService = mailService;
    }

    // =======================
    // QR PASS mapper
    // =======================
    private QrPassResponse toQrPassResponse(QrPass qr) {
        return new QrPassResponse(
                qr.getQrId(),
                (qr.getReservation() != null) ? qr.getReservation().getReservationId() : null,
                qr.getQrCode(),
                qr.isActive(),
                qr.getGeneratedAt(),
                qr.getUsedAt()
        );
    }

    // ========== USER MANAGEMENT ==========
    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(DtoMapper::toUserResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByRole(String role) {
        User.Role userRole = User.Role.valueOf(role.toUpperCase());
        return userRepository.findByRole(userRole).stream()
                .map(DtoMapper::toUserResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
        return DtoMapper.toUserResponse(user);
    }

    @Override
    public UserResponse updateUserRole(Long userId, String newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));

        User.Role role = User.Role.valueOf(newRole.toUpperCase());
        user.setRole(role);
        User updatedUser = userRepository.save(user);

        // Send notification email
        String subject = "Your account role has been updated";
        String body = "Dear " + user.getName() + ",\n\nYour account role has been updated to: " + role
                + "\n\nBest regards,\nBookFair Team";
        mailService.sendAndLog(user, null, EmailNotification.EmailType.GENERAL, subject, body);

        return DtoMapper.toUserResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));

        // Send deletion notification
        String subject = "Your BookFair account has been deleted";
        String body = "Dear " + user.getName()
                + ",\n\nYour account has been deleted by admin.\n\nBest regards,\nBookFair Team";
        mailService.sendAndLog(user, null, EmailNotification.EmailType.GENERAL, subject, body);

        userRepository.deleteById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalUsersCount() {
        return userRepository.count();
    }

    // ========== STALL MANAGEMENT ==========
    @Override
    @Transactional(readOnly = true)
    public List<StallResponse> getAllStalls() {
        return stallRepository.findAll().stream()
                .map(DtoMapper::toStallResponse)
                .toList();
    }

    @Override
    public StallResponse updateStallStatus(Long stallId, String status) {
        Stall stall = stallRepository.findById(stallId)
                .orElseThrow(() -> new NotFoundException("Stall not found with ID: " + stallId));

        Stall.Status stallStatus = Stall.Status.valueOf(status.toUpperCase());
        stall.setStatus(stallStatus);
        Stall updatedStall = stallRepository.save(stall);

        return DtoMapper.toStallResponse(updatedStall);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StallResponse> getStallsByStatus(String status) {
        Stall.Status stallStatus = Stall.Status.valueOf(status.toUpperCase());
        return stallRepository.findByStatus(stallStatus).stream()
                .map(DtoMapper::toStallResponse)
                .toList();
    }

    @Override
    public void deleteStall(Long stallId) {
        stallRepository.findById(stallId)
                .orElseThrow(() -> new NotFoundException("Stall not found with ID: " + stallId));
        stallRepository.deleteById(stallId);
    }

    @Override
    @Transactional
    public StallResponse createStall(StallResponse stallResponse) {
        // Check if stall with same code already exists
        if (stallRepository.findByStallCode(stallResponse.getStallCode()).isPresent()) {
            throw new IllegalArgumentException("Stall with code " + stallResponse.getStallCode() + " already exists");
        }

        // Create new Stall entity
        Stall newStall = new Stall();
        newStall.setStallCode(stallResponse.getStallCode());
        newStall.setHall(stallResponse.getHall());
        newStall.setSize(Stall.Size.valueOf(stallResponse.getSize().toUpperCase()));
        newStall.setPrice(stallResponse.getPrice());
        newStall.setAreaSqm(stallResponse.getAreaSqm());
        newStall.setStatus(Stall.Status.valueOf(stallResponse.getStatus().toUpperCase()));

        // Save to database
        Stall savedStall = stallRepository.save(newStall);

        // Map to response and return
        return DtoMapper.toStallResponse(savedStall);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalStallsCount() {
        return stallRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long getAvailableStallsCount() {
        return stallRepository.findByStatus(Stall.Status.AVAILABLE).size();
    }

    @Override
    @Transactional(readOnly = true)
    public long getReservedStallsCount() {
        return stallRepository.findByStatus(Stall.Status.RESERVED).size();
    }

    // ========== RESERVATION MANAGEMENT ==========
    @Override
    @Transactional(readOnly = true)
    public Page<ReservationResponse> getAllReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable)
                .map(DtoMapper::toReservationResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponse getReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found with ID: " + reservationId));
        return DtoMapper.toReservationResponse(reservation);
    }

    @Override
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found with ID: " + reservationId));

        // Free up all stalls
        List<ReservationStall> reservationStalls = reservationStallRepository
                .findByReservation_ReservationId(reservationId);
        for (ReservationStall rs : reservationStalls) {
            Stall stall = rs.getStall();
            stall.setStatus(Stall.Status.AVAILABLE);
            stallRepository.save(stall);
        }

        // Optional: also deactivate QR pass if exists
        qrPassRepository.findByReservation_ReservationId(reservationId).ifPresent(qr -> {
            qr.setActive(false);
            qrPassRepository.save(qr);
        });

        // Delete reservation
        reservationRepository.deleteById(reservationId);

        // Send cancellation email to user
        User user = reservation.getUser();
        String subject = "Your reservation #" + reservationId + " has been cancelled by admin";
        String body = "Dear " + user.getName()
                + ",\n\nYour reservation has been cancelled by administrator.\n\nBest regards,\nBookFair Team";
        mailService.sendAndLog(user, reservation, EmailNotification.EmailType.GENERAL, subject, body);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalReservationsCount() {
        return reservationRepository.count();
    }

    @Override
    public void sendReservationConfirmationEmail(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found with ID: " + reservationId));

        User user = reservation.getUser();
        if (user == null || user.getEmail() == null) {
            throw new NotFoundException("User email not found for reservation ID: " + reservationId);
        }

        String subject = "Your Reservation Confirmed - BookFairpro";
        String htmlBody = generateReservationConfirmationEmail(user.getName(), reservation);

        mailService.sendAndLogHtml(user, reservation, EmailNotification.EmailType.RESERVATION_CONFIRMATION, subject, htmlBody);
    }

    private String generateReservationConfirmationEmail(String userName, Reservation reservation) {
        // Get total stalls reserved
        int totalStalls = reservation.getReservationStalls() != null ? reservation.getReservationStalls().size() : 0;

        // Get QR pass details
        QrPass qrPass = qrPassRepository.findByReservation_ReservationId(reservation.getReservationId()).orElse(null);
        String qrCode = qrPass != null ? qrPass.getQrCode() : "N/A";

        return "<html>" +
                "<body style=\"font-family: Arial, sans-serif; color: #333;\">" +
                "<div style=\"max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;\">" +
                "<h2 style=\"color: #27ae60;\">✓ Reservation Confirmed!</h2>" +
                "<p style=\"font-size: 16px; font-weight: bold; color: #2c3e50;\">Dear " + userName + ",</p>" +
                "<p>Great news! Your reservation has been confirmed at BookFairpro. Here are your reservation details:</p>" +

                // Reservation Details Section
                "<h3 style=\"color: #2c3e50; margin-top: 25px; border-bottom: 2px solid #27ae60; padding-bottom: 10px;\">Reservation Details</h3>" +
                "<table style=\"width: 100%; border-collapse: collapse;\">" +
                "<tr style=\"background-color: #f8f9fa;\">" +
                "<td style=\"padding: 10px; border: 1px solid #ddd;\"><strong>Reservation ID</strong></td>" +
                "<td style=\"padding: 10px; border: 1px solid #ddd;\">#" + reservation.getReservationId() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"padding: 10px; border: 1px solid #ddd;\"><strong>QR Pass Code</strong></td>" +
                "<td style=\"padding: 10px; border: 1px solid #ddd;\"><span style=\"font-weight: bold; color: #e74c3c; font-family: monospace;\">" + qrCode + "</span></td>" +
                "</tr>" +
                "<tr style=\"background-color: #f8f9fa;\">" +
                "<td style=\"padding: 10px; border: 1px solid #ddd;\"><strong>Total Stalls Booked</strong></td>" +
                "<td style=\"padding: 10px; border: 1px solid #ddd;\">" + totalStalls + "</td>" +
                "</tr>" +
                "</table>" +

                // Important Notice Box
                "<div style=\"background-color: #fff3cd; padding: 15px; margin-top: 20px; border-left: 4px solid #ffc107; border-radius: 4px;\">" +
                "<p style=\"margin: 0; font-weight: bold; color: #856404;\">⚠️ Important - Please Keep Your QR Code Safe!</p>" +
                "<p style=\"margin: 10px 0 0 0; color: #333;\">" +
                "Your QR Pass Code (<strong>" + qrCode + "</strong>) is required for entry to the BookFair event. " +
                "Please keep this code in a safe place. You can show this QR code on your phone or print it for entry.\n" +
                "</p>" +
                "</div>" +

                // Next Steps
                "<h3 style=\"color: #2c3e50; margin-top: 25px; border-bottom: 2px solid #3498db; padding-bottom: 10px;\">What's Next?</h3>" +
                "<ul style=\"color: #555; line-height: 1.8;\">" +
                "<li>Save this email for your records</li>" +
                "<li>Keep your QR Pass Code safe until the event</li>" +
                "<li>Arrive early on the day of the event</li>" +
                "<li>Present your QR code at the entry gate</li>" +
                "</ul>" +

                "<p style=\"margin-top: 20px; color: #666;\">If you have any questions about your reservation, please contact our support team.</p>" +
                "<p style=\"color: #999; font-size: 12px; margin-top: 30px;\">This is an automated email. Please do not reply directly.</p>" +
                "<p style=\"color: #999; font-size: 12px;\">Best regards,<br/>BookFairPro Team</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    // ========== PAYMENT MANAGEMENT ==========
    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponse> getAllPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable)
                .map(DtoMapper::toPaymentResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found with ID: " + paymentId));
        return DtoMapper.toPaymentResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByStatus(String status) {
        Payment.PaymentStatus paymentStatus = Payment.PaymentStatus.valueOf(status.toUpperCase());
        return paymentRepository.findByPaymentStatus(paymentStatus).stream()
                .map(DtoMapper::toPaymentResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalPaymentsCount() {
        return paymentRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalPaymentsAmount() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                .mapToLong(p -> p.getAmount().longValue())
                .sum();
    }

    @Override
    @Transactional(readOnly = true)
    public long getSuccessfulPaymentsCount() {
        return paymentRepository.findByPaymentStatus(Payment.PaymentStatus.SUCCESS).size();
    }

    @Override
    @Transactional(readOnly = true)
    public long getPendingPaymentsCount() {
        return paymentRepository.findByPaymentStatus(Payment.PaymentStatus.PENDING).size();
    }

    @Override
    @Transactional(readOnly = true)
    public long getFailedPaymentsCount() {
        return paymentRepository.findByPaymentStatus(Payment.PaymentStatus.FAILED).size();
    }

    @Override
    public PaymentResponse updatePaymentStatus(Long paymentId, String status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found with ID: " + paymentId));

        Payment.PaymentStatus paymentStatus = Payment.PaymentStatus.valueOf(status.toUpperCase());
        payment.setPaymentStatus(paymentStatus);
        Payment updatedPayment = paymentRepository.save(payment);

        // Send confirmation email when status is updated to SUCCESS
        if (paymentStatus == Payment.PaymentStatus.SUCCESS) {
            sendPaymentConfirmationEmail(paymentId);
        }

        return DtoMapper.toPaymentResponse(updatedPayment);
    }

    @Override
    public PaymentResponse confirmPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found with ID: " + paymentId));

        payment.setPaymentStatus(Payment.PaymentStatus.SUCCESS);
        Payment confirmedPayment = paymentRepository.save(payment);

        return DtoMapper.toPaymentResponse(confirmedPayment);
    }

    @Override
    public void sendPaymentConfirmationEmail(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found with ID: " + paymentId));

        Reservation reservation = payment.getReservation();
        if (reservation == null) {
            throw new NotFoundException("Reservation not found for payment ID: " + paymentId);
        }

        User vendor = reservation.getUser();
        if (vendor == null || vendor.getEmail() == null) {
            throw new NotFoundException("Vendor email not found for reservation ID: " + reservation.getReservationId());
        }

        String subject = "Payment Confirmed - BookFair";
        String htmlBody = generatePaymentConfirmationEmail(vendor.getName(), payment, reservation);

        mailService.sendAndLogHtml(vendor, reservation, EmailNotification.EmailType.GENERAL, subject, htmlBody);
    }

    private String generatePaymentConfirmationEmail(String vendorName, Payment payment, Reservation reservation) {

        int totalStalls = reservation.getReservationStalls() != null
                ? reservation.getReservationStalls().size()
                : 0;

        QrPass qrPass = qrPassRepository
                .findByReservation_ReservationId(reservation.getReservationId())
                .orElse(null);

        String qrId = qrPass != null ? qrPass.getQrCode() : "N/A";

        return "<html>" +
                "<body style=\"font-family: Arial, sans-serif; color: #333;\">" +
                "<div style=\"max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;\">" +
                "<h2 style=\"color: #27ae60;\">✓ Payment Confirmed & Reservation Confirmed!</h2>" +
                "<p style=\"font-size: 16px; font-weight: bold; color: #2c3e50;\">Dear " + vendorName + ",</p>" +
                "<p>Your payment has been successfully confirmed! Your reservation is now active.</p>" +

                // Payment Details Section
                "<h3 style=\"color: #2c3e50; margin-top: 25px; border-bottom: 2px solid #27ae60; padding-bottom: 10px;\">Payment Details</h3>" +
                "<table style=\"width: 100%; border-collapse: collapse;\">" +
                "<tr style=\"background-color: #f8f9fa;\">" +
                "<td style=\"padding: 10px; border: 1px solid #ddd;\"><strong>Payment ID</strong></td>" +
                "<td style=\"padding: 10px; border: 1px solid #ddd;\">#" + payment.getPaymentId() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"padding: 10px; border: 1px solid #ddd;\"><strong>Amount</strong></td>" +
                "<td style=\"padding: 10px; border: 1px solid #ddd;\">$" + String.format("%.2f", payment.getAmount()) + "</td>" +
                "</tr>" +
                "<tr style=\"background-color: #f8f9fa;\">" +
                "<td style=\"padding: 10px; border: 1px solid #ddd;\"><strong>Payment Method</strong></td>" +
                "<td style=\"padding: 10px; border: 1px solid #ddd;\">" + payment.getPaymentMethod() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"padding: 10px; border: 1px solid #ddd;\"><strong>Status</strong></td>" +
                "<td style=\"padding: 10px; border: 1px solid #ddd;\"><span style=\"color: #27ae60; font-weight: bold;\">✓ SUCCESS</span></td>" +
                "</tr>" +
                "</table>" +

                // Reservation Details Section
                "<h3 style=\"color: #2c3e50; margin-top: 25px; border-bottom: 2px solid #3498db; padding-bottom: 10px;\">Your Reservation Confirmed!</h3>" +
                "<table style=\"width: 100%; border-collapse: collapse;\">" +
                "<tr style=\"background-color: #f8f9fa;\">" +
                "<td style=\"padding: 10px; border: 1px solid #ddd;\"><strong>Reservation ID</strong></td>" +
                "<td style=\"padding: 10px; border: 1px solid #ddd;\">#" + reservation.getReservationId() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"padding: 10px; border: 1px solid #ddd;\"><strong>QR Pass ID</strong></td>" +
                "<td style=\"padding: 10px; border: 1px solid #ddd;\"><span style=\"font-weight: bold; color: #e74c3c;\">" + qrId + "</span></td>" +
                "</tr>" +
                "<tr style=\"background-color: #f8f9fa;\">" +
                "<td style=\"padding: 10px; border: 1px solid #ddd;\"><strong>Total Stalls</strong></td>" +
                "<td style=\"padding: 10px; border: 1px solid #ddd;\">" + totalStalls + "</td>" +
                "</tr>" +
                "</table>" +

                // ✅ QR IMAGE SECTION (INLINE)
                "<div style=\"text-align:center; margin-top: 18px;\">" +
                "<p style=\"margin: 0 0 10px 0;\"><strong>Your QR Code:</strong></p>" +
                "<img src=\"cid:qrImage\" style=\"width:260px;height:260px;border:1px solid #ddd;padding:8px;border-radius:10px;\"/>" +
                "<p style=\"margin-top:10px; color:#555;\">Show this QR at the entry.</p>" +
                "</div>" +

                // Important Information Box
                "<div style=\"background-color: #ecf0f1; padding: 15px; margin-top: 20px; border-left: 4px solid #e74c3c; border-radius: 4px;\">" +
                "<p style=\"margin: 0; font-weight: bold; color: #2c3e50;\">⚠️ Important:</p>" +
                "<p style=\"margin: 10px 0 0 0; color: #555;\">Please keep your QR Pass ID <strong>" + qrId + "</strong> safe. You will need it for entry to the BookFair event.</p>" +
                "</div>" +

                "<p style=\"margin-top: 20px; color: #666;\">Thank you for booking with BookFair. We look forward to seeing you at the event!</p>" +
                "<p style=\"color: #999; font-size: 12px; margin-top: 30px;\">This is an automated email. Please do not reply directly.</p>" +
                "<p style=\"color: #999; font-size: 12px;\">BookFair Team</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }


    // ========== EMAIL NOTIFICATIONS ==========
    @Override
    @Transactional(readOnly = true)
    public Page<EmailNotificationResponse> getAllEmailNotifications(Pageable pageable) {
        return emailNotificationRepository.findAll(pageable)
                .map(DtoMapper::toEmailNotificationResponse);
    }

    @Override
    public void resendEmailNotification(Long emailNotificationId) {
        EmailNotification notification = emailNotificationRepository.findById(emailNotificationId)
                .orElseThrow(() ->
                        new NotFoundException("Email notification not found with ID: " + emailNotificationId));

        User user = notification.getUser();
        Reservation reservation = notification.getReservation();

        // Resend the email
        mailService.sendAndLog(user, reservation, notification.getEmailType(), notification.getSubject(), "");
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailNotificationResponse> getFailedEmailNotifications() {
        return emailNotificationRepository.findByEmailStatus(EmailNotification.EmailStatus.FAILED).stream()
                .map(DtoMapper::toEmailNotificationResponse)
                .toList();
    }

    // ========== QR PASS MANAGEMENT ==========
    @Override
    @Transactional(readOnly = true)
    public Page<QrPassResponse> getAllQrPasses(Pageable pageable) {
        return qrPassRepository.findAll(pageable).map(this::toQrPassResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public QrPassResponse getQrPassById(Long qrId) {
        QrPass qr = qrPassRepository.findById(qrId)
                .orElseThrow(() -> new NotFoundException("QR Pass not found with ID: " + qrId));
        return toQrPassResponse(qr);
    }

    @Override
    @Transactional(readOnly = true)
    public QrPassResponse getQrPassByReservationId(Long reservationId) {
        QrPass qr = qrPassRepository.findByReservation_ReservationId(reservationId)
                .orElseThrow(() -> new NotFoundException("QR Pass not found for reservation ID: " + reservationId));
        return toQrPassResponse(qr);
    }

    @Override
    @Transactional(readOnly = true)
    public QrPassResponse getQrPassByQrCode(String qrCode) {
        QrPass qr = qrPassRepository.findByQrCode(qrCode)
                .orElseThrow(() -> new NotFoundException("QR Pass not found for qrCode: " + qrCode));
        return toQrPassResponse(qr);
    }

    @Override
    public QrPassResponse activateQrPass(Long qrId) {
        QrPass qr = qrPassRepository.findById(qrId)
                .orElseThrow(() -> new NotFoundException("QR Pass not found with ID: " + qrId));

        qr.setActive(true);
        return toQrPassResponse(qrPassRepository.save(qr));
    }

    @Override
    public QrPassResponse deactivateQrPass(Long qrId) {
        QrPass qr = qrPassRepository.findById(qrId)
                .orElseThrow(() -> new NotFoundException("QR Pass not found with ID: " + qrId));

        qr.setActive(false);
        return toQrPassResponse(qrPassRepository.save(qr));
    }

    @Override
    public QrPassResponse markQrPassAsUsed(Long qrId) {
        QrPass qr = qrPassRepository.findById(qrId)
                .orElseThrow(() -> new NotFoundException("QR Pass not found with ID: " + qrId));

        if (qr.getUsedAt() != null) {
            throw new IllegalStateException("QR Pass already used at: " + qr.getUsedAt());
        }

        qr.setUsedAt(LocalDateTime.now());
        qr.setActive(false);

        return toQrPassResponse(qrPassRepository.save(qr));
    }

    @Override
    public void deleteQrPass(Long qrId) {
        QrPass qr = qrPassRepository.findById(qrId)
                .orElseThrow(() -> new NotFoundException("QR Pass not found with ID: " + qrId));
        qrPassRepository.delete(qr);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalQrPassesCount() {
        return qrPassRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long getActiveQrPassesCount() {
        return qrPassRepository.countByActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public long getInactiveQrPassesCount() {
        return qrPassRepository.countByActiveFalse();
    }

    // ========== DASHBOARD STATISTICS ==========
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> stats = new LinkedHashMap<>();

        // User Statistics
        Map<String, Object> userStats = new LinkedHashMap<>();
        userStats.put("totalUsers", getTotalUsersCount());
        userStats.put("adminCount", userRepository.findByRole(User.Role.ADMIN).size());
        userStats.put("vendorCount", userRepository.findByRole(User.Role.VENDOR).size());
        userStats.put("publisherCount", userRepository.findByRole(User.Role.PUBLISHER).size());
        userStats.put("userCount", userRepository.findByRole(User.Role.USER).size());
        stats.put("users", userStats);

        // Stall Statistics
        Map<String, Object> stallStats = new LinkedHashMap<>();
        stallStats.put("totalStalls", getTotalStallsCount());
        stallStats.put("availableStalls", getAvailableStallsCount());
        stallStats.put("reservedStalls", getReservedStallsCount());
        stats.put("stalls", stallStats);

        // Reservation Statistics
        Map<String, Object> reservationStats = new LinkedHashMap<>();
        reservationStats.put("totalReservations", getTotalReservationsCount());
        stats.put("reservations", reservationStats);

        // Payment Statistics
        Map<String, Object> paymentStats = new LinkedHashMap<>();
        paymentStats.put("totalPayments",
                getSuccessfulPaymentsCount() + getPendingPaymentsCount() + getFailedPaymentsCount());
        paymentStats.put("successfulPayments", getSuccessfulPaymentsCount());
        paymentStats.put("pendingPayments", getPendingPaymentsCount());
        paymentStats.put("failedPayments", getFailedPaymentsCount());
        paymentStats.put("totalAmount", getTotalPaymentsAmount());
        stats.put("payments", paymentStats);

        // QR Pass Statistics (NEW)
        Map<String, Object> qrStats = new LinkedHashMap<>();
        qrStats.put("totalQrPasses", getTotalQrPassesCount());
        qrStats.put("activeQrPasses", getActiveQrPassesCount());
        qrStats.put("inactiveQrPasses", getInactiveQrPassesCount());
        stats.put("qrPasses", qrStats);

        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getSystemMetrics() {
        Map<String, Object> metrics = new LinkedHashMap<>();

        metrics.put("timestamp", new Date());
        metrics.put("status", "ACTIVE");
        metrics.put("databaseConnected", true);
        metrics.put("totalRecords", getTotalUsersCount() + getTotalStallsCount() + getTotalReservationsCount());
        metrics.put("failedEmails", getFailedEmailNotifications().size());

        return metrics;
    }
}

