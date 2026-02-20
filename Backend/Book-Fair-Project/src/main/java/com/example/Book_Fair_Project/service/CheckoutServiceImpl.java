package com.example.Book_Fair_Project.service;

import com.example.Book_Fair_Project.dto.checkout.ReservationCheckoutRequest;
import com.example.Book_Fair_Project.dto.checkout.ReservationCheckoutResponse;
import com.example.Book_Fair_Project.dto.payment.PaymentCreateRequest;
import com.example.Book_Fair_Project.dto.payment.PaymentResponse;
import com.example.Book_Fair_Project.dto.qr.QrPassResponse;
import com.example.Book_Fair_Project.dto.reservation.ReservationCreateRequest;
import com.example.Book_Fair_Project.dto.reservation.ReservationResponse;
import com.example.Book_Fair_Project.exception.BadRequestException;
import com.example.Book_Fair_Project.exception.NotFoundException;
import com.example.Book_Fair_Project.model.EmailNotification;
import com.example.Book_Fair_Project.model.Reservation;
import com.example.Book_Fair_Project.model.Stall;
import com.example.Book_Fair_Project.model.User;
import com.example.Book_Fair_Project.repository.ReservationRepository;
import com.example.Book_Fair_Project.repository.StallRepository;
import com.example.Book_Fair_Project.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class CheckoutServiceImpl implements CheckoutService {

    private final UserRepository userRepository;
    private final StallRepository stallRepository;
    private final ReservationRepository reservationRepository;

    private final UserGenreService userGenreService;
    private final ReservationService reservationService;
    private final PaymentService paymentService;
    private final QrPassService qrPassService;
    private final MailService mailService;

    public CheckoutServiceImpl(
            UserRepository userRepository,
            StallRepository stallRepository,
            ReservationRepository reservationRepository,
            UserGenreService userGenreService,
            ReservationService reservationService,
            PaymentService paymentService,
            QrPassService qrPassService,
            MailService mailService) {
        this.userRepository = userRepository;
        this.stallRepository = stallRepository;
        this.reservationRepository = reservationRepository;
        this.userGenreService = userGenreService;
        this.reservationService = reservationService;
        this.paymentService = paymentService;
        this.qrPassService = qrPassService;
        this.mailService = mailService;
    }

    @Override
    public ReservationCheckoutResponse reservePayAndGenerateQr(ReservationCheckoutRequest request) {

        // ✅ strict validation (prevents random 400)
        if (request == null)
            throw new BadRequestException("Request body is missing");
        if (request.getUserId() == null)
            throw new BadRequestException("User ID is required");
        if (request.getStallIds() == null || request.getStallIds().isEmpty())
            throw new BadRequestException("At least one stallId is required");
        if (request.getGenres() == null || request.getGenres().isEmpty())
            throw new BadRequestException("Select at least 1 genre");
        if (request.getPaymentMethod() == null || request.getPaymentMethod().isBlank())
            throw new BadRequestException("Payment method is required");

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found: " + request.getUserId()));

        // ✅ Enforce MAX 3 stalls per user limit
        final int MAX_STALLS_PER_USER = 3;
        long existingStallCount = reservationRepository.countTotalStallsByUserId(request.getUserId());
        int newStallCount = request.getStallIds().size();
        if (existingStallCount + newStallCount > MAX_STALLS_PER_USER) {
            long remaining = MAX_STALLS_PER_USER - existingStallCount;
            if (remaining <= 0) {
                throw new BadRequestException(
                        "You have already booked the maximum of " + MAX_STALLS_PER_USER + " stalls. "
                                + "You cannot book any more stalls.");
            } else {
                throw new BadRequestException(
                        "You can only book " + remaining + " more stall(s). "
                                + "You currently have " + existingStallCount + " stall(s) booked "
                                + "and the maximum is " + MAX_STALLS_PER_USER + ".");
            }
        }

        // 1) Save Genres
        userGenreService.replaceGenres(request.getUserId(), request.getGenres());

        // 2) Create Reservation (using YOUR existing ReservationServiceImpl method)
        ReservationCreateRequest r = new ReservationCreateRequest();
        r.setUserId(request.getUserId());
        r.setStallIds(request.getStallIds());

        ReservationResponse reservationRes = reservationService.createReservation(r);

        // Need Reservation entity for QR email logging
        Reservation reservation = reservationRepository.findById(reservationRes.getReservationId())
                .orElseThrow(
                        () -> new NotFoundException("Reservation not found: " + reservationRes.getReservationId()));

        // 3) Calculate amount (based on stall codes A/B/C)
        BigDecimal amount = calcAmountFromStalls(request.getStallIds());

        // 4) Process Payment (PENDING + sends email inside PaymentServiceImpl)
        PaymentCreateRequest payReq = new PaymentCreateRequest();
        payReq.setReservationId(reservation.getReservationId());
        payReq.setAmount(amount);
        payReq.setPaymentMethod(request.getPaymentMethod());
        payReq.setPaymentDetails(
                (request.getPaymentDetails() == null || request.getPaymentDetails().isBlank())
                        ? "Auto checkout from UI"
                        : request.getPaymentDetails());

        PaymentResponse paymentRes = paymentService.processPayment(payReq);

        // 5) Generate QR
        QrPassResponse qrRes = qrPassService.generateQrPass(reservation.getReservationId());

        // 6) Send QR email (optional but requested)
        String subject = "QR Pass Generated - BookFairPro";
        String body = "Hello " + user.getName() + ",\n\n"
                + "Your QR pass is generated.\n"
                + "Reservation ID: " + reservation.getReservationId() + "\n"
                + "QR Code: " + qrRes.getQrCode() + "\n"
                + "Selected Genres: " + String.join(", ", request.getGenres()) + "\n\n"
                + "BookFairPro Team";

        mailService.sendAndLog(user, reservation, EmailNotification.EmailType.QR_PASS, subject, body);

        return new ReservationCheckoutResponse(reservationRes, paymentRes, qrRes);
    }

    @Override
    public ReservationResponse reserve(ReservationCheckoutRequest request) {
        // Validation
        if (request == null)
            throw new BadRequestException("Request body is missing");
        if (request.getUserId() == null)
            throw new BadRequestException("User ID is required");
        if (request.getStallIds() == null || request.getStallIds().isEmpty())
            throw new BadRequestException("At least one stallId is required");
        if (request.getGenres() == null || request.getGenres().isEmpty())
            throw new BadRequestException("Select at least 1 genre");

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found: " + request.getUserId()));

        // ✅ Enforce MAX 3 stalls per user limit
        final int MAX_STALLS_PER_USER = 3;
        long existingStallCount = reservationRepository.countTotalStallsByUserId(request.getUserId());
        int newStallCount = request.getStallIds().size();
        if (existingStallCount + newStallCount > MAX_STALLS_PER_USER) {
            long remaining = MAX_STALLS_PER_USER - existingStallCount;
            if (remaining <= 0) {
                throw new BadRequestException(
                        "You have already booked the maximum of " + MAX_STALLS_PER_USER + " stalls. "
                                + "You cannot book any more stalls.");
            } else {
                throw new BadRequestException(
                        "You can only book " + remaining + " more stall(s). "
                                + "You currently have " + existingStallCount + " stall(s) booked "
                                + "and the maximum is " + MAX_STALLS_PER_USER + ".");
            }
        }

        // 1) Save Genres
        userGenreService.replaceGenres(request.getUserId(), request.getGenres());

        // 2) Create Reservation
        ReservationCreateRequest r = new ReservationCreateRequest();
        r.setUserId(request.getUserId());
        r.setStallIds(request.getStallIds());

        return reservationService.createReservation(r);
    }

    @Override
    public PaymentResponse pay(PaymentCreateRequest request) {
        return paymentService.processPayment(request);
    }

    @Override
    public QrPassResponse generateQr(Long reservationId) {
        return qrPassService.generateQrPass(reservationId);
    }

    private BigDecimal calcAmountFromStalls(List<Long> stallIds) {
        List<Stall> stalls = stallRepository.findAllById(stallIds);
        if (stalls.size() != stallIds.size())
            throw new BadRequestException("Some stalls not found");

        BigDecimal total = BigDecimal.ZERO;
        for (Stall s : stalls) {
            String code = s.getStallCode();
            if (code == null)
                throw new BadRequestException("Stall code missing for stallId: " + s.getStallId());

            BigDecimal price;
            if (code.startsWith("A"))
                price = BigDecimal.valueOf(2500);
            else if (code.startsWith("B"))
                price = BigDecimal.valueOf(5000);
            else
                price = BigDecimal.valueOf(7500);

            total = total.add(price);
        }
        return total;
    }
}