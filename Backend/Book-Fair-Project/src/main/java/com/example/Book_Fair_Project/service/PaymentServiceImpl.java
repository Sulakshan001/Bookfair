package com.example.Book_Fair_Project.service;

import com.example.Book_Fair_Project.dto.payment.PaymentCreateRequest;
import com.example.Book_Fair_Project.dto.payment.PaymentResponse;
import com.example.Book_Fair_Project.exception.BadRequestException;
import com.example.Book_Fair_Project.exception.NotFoundException;
import com.example.Book_Fair_Project.mapper.DtoMapper;
import com.example.Book_Fair_Project.model.EmailNotification;
import com.example.Book_Fair_Project.model.Payment;
import com.example.Book_Fair_Project.model.Reservation;
import com.example.Book_Fair_Project.repository.PaymentRepository;
import com.example.Book_Fair_Project.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final MailService mailService;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            ReservationRepository reservationRepository,
            MailService mailService
    ) {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
        this.mailService = mailService;
    }

    @Override
    public PaymentResponse processPayment(PaymentCreateRequest request) {
        if (request == null) throw new BadRequestException("Request body is missing");
        if (request.getReservationId() == null) throw new BadRequestException("Reservation ID is required");
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Amount must be greater than zero");
        }
        if (request.getPaymentMethod() == null) throw new BadRequestException("Payment method is required");

        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new NotFoundException("Reservation not found: " + request.getReservationId()));

        Payment payment = new Payment();
        payment.setReservation(reservation);
        payment.setAmount(request.getAmount());

        // Parse PaymentMethod from String
        try {
            Payment.PaymentMethod method = Payment.PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase());
            payment.setPaymentMethod(method);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid payment method: " + request.getPaymentMethod());
        }

        payment.setPaymentStatus(Payment.PaymentStatus.PENDING);
        payment.setPaymentDetails(request.getPaymentDetails());

        Payment savedPayment = paymentRepository.save(payment);

        // Send payment pending email
        sendPaymentPendingEmail(reservation, savedPayment);

        return DtoMapper.toPaymentResponse(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found: " + paymentId));
        return DtoMapper.toPaymentResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getReservationPayments(Long reservationId) {
        reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found: " + reservationId));

        return paymentRepository.findByReservation_ReservationId(reservationId).stream()
                .map(DtoMapper::toPaymentResponse)
                .toList();
    }

    @Override
    public PaymentResponse markPaymentSuccessful(Long paymentId, String referenceNumber) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found: " + paymentId));

        payment.setPaymentStatus(Payment.PaymentStatus.SUCCESS);
        payment.setReferenceNumber(referenceNumber);

        Payment savedPayment = paymentRepository.save(payment);

        // Send payment success email
        sendPaymentSuccessEmail(savedPayment.getReservation(), savedPayment);

        return DtoMapper.toPaymentResponse(savedPayment);
    }

    @Override
    public PaymentResponse markPaymentFailed(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found: " + paymentId));

        payment.setPaymentStatus(Payment.PaymentStatus.FAILED);
        Payment savedPayment = paymentRepository.save(payment);

        // Send payment failed email
        sendPaymentFailedEmail(savedPayment.getReservation(), savedPayment);

        return DtoMapper.toPaymentResponse(savedPayment);
    }

    private void sendPaymentPendingEmail(Reservation reservation, Payment payment) {

        String subject = "Payment Pending - BookFair";

        String customerName = reservation.getUser() != null ? reservation.getUser().getName() : "Customer";
        String paymentId = payment.getPaymentId() != null ? payment.getPaymentId().toString() : "N/A";
        String amount = payment.getAmount() != null ? payment.getAmount().toString() : "0.00";
        String method = payment.getPaymentMethod() != null ? payment.getPaymentMethod().name() : "N/A";
        String reservationId = reservation.getReservationId() != null ? reservation.getReservationId().toString() : "N/A";

        String htmlBody = """
        <div style="font-family:Arial,sans-serif;padding:20px;background:#f4f6f9;">
          <div style="max-width:600px;margin:auto;background:#ffffff;padding:30px;border-radius:10px;border:1px solid #e8edf3;">
            
            <h2 style="color:#0B4C5F;margin:0 0 10px;">Payment Pending</h2>
            <p style="margin:0 0 14px;">Hello <strong>%s</strong>,</p>

            <p style="margin:0 0 18px;">
              Your payment is currently
              <strong style="color:#e67e22;">PENDING</strong>.
            </p>

            <div style="height:1px;background:#e8edf3;margin:18px 0;"></div>

            <p style="margin:0 0 10px;"><strong>Payment Details</strong></p>
            <table style="width:100%%;border-collapse:collapse;">
              <tr>
                <td style="padding:8px 0;color:#556;">Reservation ID</td>
                <td style="padding:8px 0;text-align:right;"><strong>%s</strong></td>
              </tr>
              <tr>
                <td style="padding:8px 0;color:#556;">Payment ID</td>
                <td style="padding:8px 0;text-align:right;"><strong>%s</strong></td>
              </tr>
              <tr>
                <td style="padding:8px 0;color:#556;">Amount</td>
                <td style="padding:8px 0;text-align:right;"><strong>LKR %s</strong></td>
              </tr>
              <tr>
                <td style="padding:8px 0;color:#556;">Method</td>
                <td style="padding:8px 0;text-align:right;"><strong>%s</strong></td>
              </tr>
            </table>

            <div style="margin-top:18px;padding:12px 14px;background:#fff7ec;border:1px solid #ffe1b8;border-radius:8px;">
              <span style="color:#a35a00;">
                Please complete the payment to confirm your stall reservation.
              </span>
            </div>

            <p style="margin:22px 0 0;">Regards,<br><strong>BookFairPro Team</strong></p>
          </div>
        </div>
        """.formatted(customerName, reservationId, paymentId, amount, method);

        mailService.sendAndLogHtml(
                reservation.getUser(),
                reservation,
                EmailNotification.EmailType.GENERAL,
                subject,
                htmlBody
        );
    }

    private void sendPaymentSuccessEmail(Reservation reservation, Payment payment) {

        String subject = "Payment Successful - BookFair";

        String customerName = reservation.getUser() != null ? reservation.getUser().getName() : "Customer";
        String paymentId = payment.getPaymentId() != null ? payment.getPaymentId().toString() : "N/A";
        String amount = payment.getAmount() != null ? payment.getAmount().toString() : "0.00";
        String ref = payment.getReferenceNumber() != null ? payment.getReferenceNumber() : "N/A";
        String reservationId = reservation.getReservationId() != null ? reservation.getReservationId().toString() : "N/A";

        String htmlBody = """
        <div style="font-family:Arial,sans-serif;padding:20px;background:#f4f6f9;">
          <div style="max-width:600px;margin:auto;background:#ffffff;padding:30px;border-radius:10px;border:1px solid #e8edf3;">
            
            <h2 style="color:#27ae60;margin:0 0 10px;">Payment Successful 🎉</h2>
            <p style="margin:0 0 14px;">Hello <strong>%s</strong>,</p>

            <p style="margin:0 0 18px;">
              Your payment has been successfully processed.
            </p>

            <div style="height:1px;background:#e8edf3;margin:18px 0;"></div>

            <p style="margin:0 0 10px;"><strong>Transaction Details</strong></p>
            <table style="width:100%%;border-collapse:collapse;">
              <tr>
                <td style="padding:8px 0;color:#556;">Reservation ID</td>
                <td style="padding:8px 0;text-align:right;"><strong>%s</strong></td>
              </tr>
              <tr>
                <td style="padding:8px 0;color:#556;">Payment ID</td>
                <td style="padding:8px 0;text-align:right;"><strong>%s</strong></td>
              </tr>
              <tr>
                <td style="padding:8px 0;color:#556;">Amount</td>
                <td style="padding:8px 0;text-align:right;"><strong>LKR %s</strong></td>
              </tr>
              <tr>
                <td style="padding:8px 0;color:#556;">Reference Number</td>
                <td style="padding:8px 0;text-align:right;"><strong>%s</strong></td>
              </tr>
            </table>

            <div style="margin-top:18px;padding:12px 14px;background:#eefbf2;border:1px solid #c9f0d6;border-radius:8px;">
              <span style="color:#1f7a3b;">
                Your stall reservation is now confirmed. We look forward to seeing you at BookFairPro.
              </span>
            </div>

            <p style="margin:22px 0 0;">Best Regards,<br><strong>BookFairPro Team</strong></p>
          </div>
        </div>
        """.formatted(customerName, reservationId, paymentId, amount, ref);

        mailService.sendAndLogHtml(
                reservation.getUser(),
                reservation,
                EmailNotification.EmailType.GENERAL,
                subject,
                htmlBody
        );
    }




    private void sendPaymentFailedEmail(Reservation reservation, Payment payment) {
        String subject = "Payment Failed - BookFairPro";
        String body = "Hello " + reservation.getUser().getName() + ",\n\n"
                + "Your payment has failed.\n"
                + "Payment ID: " + payment.getPaymentId() + "\n"
                + "Amount: " + payment.getAmount() + "\n\n"
                + "Please try again or contact support.\n\n"
                + "BookFairPro Team";

        mailService.sendAndLog(reservation.getUser(), reservation, EmailNotification.EmailType.GENERAL, subject, body);
    }
}
