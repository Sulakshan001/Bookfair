package com.example.Book_Fair_Project.service;

import com.example.Book_Fair_Project.dto.qr.QrPassResponse;
import com.example.Book_Fair_Project.model.QrPass;
import com.example.Book_Fair_Project.model.Reservation;
import com.example.Book_Fair_Project.repository.QrPassRepository;
import com.example.Book_Fair_Project.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class QrPassServiceImpl implements QrPassService {

    private final QrPassRepository qrPassRepository;
    private final ReservationRepository reservationRepository;

    public QrPassServiceImpl(QrPassRepository qrPassRepository,
                             ReservationRepository reservationRepository) {
        this.qrPassRepository = qrPassRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public QrPassResponse generateQrPass(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found: " + reservationId));

        // Optional: one reservation -> one QR rule
        qrPassRepository.findByReservation_ReservationId(reservationId)
                .ifPresent(existing -> {
                    throw new RuntimeException("QR Pass already generated for reservation: " + reservationId);
                });


        String qrCode = "BFQR-" + UUID.randomUUID(); // unique QR string

        QrPass qrPass = new QrPass();
        qrPass.setReservation(reservation);
        qrPass.setQrCode(qrCode);
        qrPass.setActive(true);
        // generatedAt auto by @CreationTimestamp

        QrPass saved = qrPassRepository.save(qrPass);

        // -----------------------------
        // SEND MAIL to Vendor & Publisher
        // -----------------------------
        // Note: Email functionality can be added here when vendor/publisher info is available
        // Currently, vendor and publisher entities are not directly linked to Reservation model

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public QrPassResponse getQrPassByReservation(Long reservationId) {
        QrPass qrPass = qrPassRepository.findByReservation_ReservationId(reservationId)
                .orElseThrow(() -> new RuntimeException("QR Pass not found for reservation: " + reservationId));
        return toResponse(qrPass);
    }

    @Override
    public QrPassResponse markQrAsUsed(Long qrId) {
        QrPass qr = qrPassRepository.findById(qrId)
                .orElseThrow(() -> new RuntimeException("QR Pass not found: " + qrId));

        if (!qr.isActive()) {
            throw new RuntimeException("QR already inactive/used.");
        }

        qr.setActive(false);
        qr.setUsedAt(LocalDateTime.now());

        return toResponse(qrPassRepository.save(qr));
    }

    private QrPassResponse toResponse(QrPass qr) {
        return new QrPassResponse(
                qr.getQrId(),
                qr.getReservation().getReservationId(), // ensure Reservation has getReservationId()
                qr.getQrCode(),
                qr.isActive(),
                qr.getGeneratedAt(),
                qr.getUsedAt()
        );
    }
}
