package com.example.Book_Fair_Project.service;

import com.example.Book_Fair_Project.dto.reservation.ReservationCreateRequest;
import com.example.Book_Fair_Project.dto.reservation.ReservationResponse;
import com.example.Book_Fair_Project.exception.BadRequestException;
import com.example.Book_Fair_Project.exception.NotFoundException;
import com.example.Book_Fair_Project.mapper.DtoMapper;
import com.example.Book_Fair_Project.model.*;
import com.example.Book_Fair_Project.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationStallRepository reservationStallRepository;
    private final UserRepository userRepository;
    private final StallRepository stallRepository;
    private final MailService mailService;

    public ReservationServiceImpl(
            ReservationRepository reservationRepository,
            ReservationStallRepository reservationStallRepository,
            UserRepository userRepository,
            StallRepository stallRepository,
            MailService mailService
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationStallRepository = reservationStallRepository;
        this.userRepository = userRepository;
        this.stallRepository = stallRepository;
        this.mailService = mailService;
    }

    @Override
    public ReservationResponse createReservation(ReservationCreateRequest request) {
        if (request == null) throw new BadRequestException("Request body is missing");
        if (request.getUserId() == null) throw new BadRequestException("User ID is required");
        if (request.getStallIds() == null || request.getStallIds().isEmpty()) {
            throw new BadRequestException("At least one stall ID is required");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found: " + request.getUserId()));

        List<Stall> stalls = new ArrayList<>();
        for (Long stallId : request.getStallIds()) {
            Stall stall = stallRepository.findById(stallId)
                    .orElseThrow(() -> new NotFoundException("Stall not found: " + stallId));

            if (stall.getStatus() == Stall.Status.RESERVED) {
                throw new BadRequestException("Stall " + stall.getStallCode() + " is already reserved");
            }
            stalls.add(stall);
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setQrId(generateQrId());

        Reservation savedReservation = reservationRepository.save(reservation);

        List<ReservationStall> joinRows = new ArrayList<>();

        for (Stall stall : stalls) {
            ReservationStall rs = new ReservationStall();
            rs.setReservation(savedReservation);
            rs.setStall(stall);

            rs.setId(new ReservationStallId(savedReservation.getReservationId(), stall.getStallId()));

            joinRows.add(reservationStallRepository.save(rs));

            stall.setStatus(Stall.Status.RESERVED);
            stallRepository.save(stall);
        }

        savedReservation.getReservationStalls().clear();
        savedReservation.getReservationStalls().addAll(joinRows);

        sendReservationConfirmationEmail(user, savedReservation);

        return DtoMapper.toReservationResponse(savedReservation);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponse getReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found: " + reservationId));
        return DtoMapper.toReservationResponse(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> getUserReservations(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        return reservationRepository.findByUser_UserId(userId).stream()
                .map(DtoMapper::toReservationResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(DtoMapper::toReservationResponse)
                .toList();
    }

    @Override
    public ReservationResponse cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found: " + reservationId));

        // Free up stalls
        for (ReservationStall rs : reservation.getReservationStalls()) {
            Stall stall = rs.getStall();
            stall.setStatus(Stall.Status.AVAILABLE);
            stallRepository.save(stall);
        }

        reservationRepository.delete(reservation);

        sendReservationCancellationEmail(reservation.getUser(), reservation);

        return DtoMapper.toReservationResponse(reservation);
    }

    private void sendReservationConfirmationEmail(User user, Reservation reservation) {
        String subject = "Reservation Confirmed - BookFairPro";
        String body = "Hello " + user.getName() + ",\n\n"
                + "Your reservation has been confirmed!\n"
                + "Reservation ID: " + reservation.getReservationId() + "\n"
                + "QR ID: " + reservation.getQrId() + "\n"
                + "Total Stalls: " + reservation.getReservationStalls().size() + "\n\n"
                + "Please keep your QR ID for entry.\n\n"
                + "BookFairPro Team";

        mailService.sendAndLog(user, reservation,
                EmailNotification.EmailType.RESERVATION_CONFIRMATION, subject, body);
    }

    private void sendReservationCancellationEmail(User user, Reservation reservation) {
        String subject = "Reservation Cancelled - BookFairPro";
        String body = "Hello " + user.getName() + ",\n\n"
                + "Your reservation has been cancelled.\n"
                + "Reservation ID: " + reservation.getReservationId() + "\n\n"
                + "If you have any questions, please contact support.\n\n"
                + "BookFairPro Team";

        mailService.sendAndLog(user, reservation,
                EmailNotification.EmailType.GENERAL, subject, body);
    }

    private String generateQrId() {
        return "RES-" + UUID.randomUUID();
    }
}
