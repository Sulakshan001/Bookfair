package com.example.Book_Fair_Project.mapper;




import com.example.Book_Fair_Project.dto.email.EmailNotificationResponse;
import com.example.Book_Fair_Project.dto.otp.UserOtpResponse;
import com.example.Book_Fair_Project.dto.payment.PaymentResponse;
import com.example.Book_Fair_Project.dto.qr.QrPassResponse;
import com.example.Book_Fair_Project.dto.reservation.ReservationItemResponse;
import com.example.Book_Fair_Project.dto.reservation.ReservationResponse;
import com.example.Book_Fair_Project.dto.stall.StallResponse;
import com.example.Book_Fair_Project.dto.user.UserResponse;
import com.example.Book_Fair_Project.model.*;

import java.util.List;
import java.util.stream.Collectors;

public class DtoMapper {

    private DtoMapper() {}

    public static UserResponse toUserResponse(User u) {
        return new UserResponse(
                u.getUserId(),
                u.getName(),
                u.getEmail(),
                u.getBusinessName(),
                u.getRole() != null ? u.getRole().name() : null,
                u.isEmailVerified(),
                u.getCreatedAt()
        );
    }

    public static StallResponse toStallResponse(Stall s) {
        return new StallResponse(
                s.getStallId(),
                s.getStallCode(),
                s.getSize() != null ? s.getSize().name() : null,
                s.getStatus() != null ? s.getStatus().name() : null,
                s.getHall(),
                s.getPrice(),
                s.getAreaSqm()
        );
    }

    public static ReservationItemResponse toReservationItemResponse(Stall s) {
        return new ReservationItemResponse(
                s.getStallId(),
                s.getStallCode(),
                s.getSize() != null ? s.getSize().name() : null
        );
    }

    public static List<ReservationItemResponse> toReservationItems(List<ReservationStall> rs) {
        if (rs == null) return List.of();
        return rs.stream()
                .map(x -> toReservationItemResponse(x.getStall()))
                .collect(Collectors.toList());
    }

    public static PaymentResponse toPaymentResponse(Payment p) {
        return new PaymentResponse(
                p.getPaymentId(),
                p.getReservation() != null ? p.getReservation().getReservationId() : null,
                p.getAmount(),
                p.getPaymentMethod() != null ? p.getPaymentMethod().name() : null,
                p.getPaymentStatus() != null ? p.getPaymentStatus().name() : null,
                p.getReferenceNumber(),
                p.getPaymentDetails(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }

    public static QrPassResponse toQrPassResponse(QrPass q) {
        return new QrPassResponse(
                q.getQrId(),
                q.getReservation() != null ? q.getReservation().getReservationId() : null,
                q.getQrCode(),
                q.isActive(),
                q.getGeneratedAt(),
                q.getUsedAt()
        );
    }

    public static EmailNotificationResponse toEmailNotificationResponse(EmailNotification e) {
        return new EmailNotificationResponse(
                e.getEmailId(),
                e.getUser() != null ? e.getUser().getUserId() : null,
                e.getReservation() != null ? e.getReservation().getReservationId() : null,
                e.getEmailType() != null ? e.getEmailType().name() : null,
                e.getEmailStatus() != null ? e.getEmailStatus().name() : null,
                e.getSubject(),
                e.getSentAt()
        );
    }

    public static UserOtpResponse toUserOtpResponse(UserOtp o) {
        return new UserOtpResponse(
                o.getOtpId(),
                o.getUser() != null ? o.getUser().getUserId() : null,
                o.getOtpCode(),
                o.getOtpType() != null ? o.getOtpType().name() : null,
                o.getExpiresAt(),
                o.isUsed(),
                o.getCreatedAt()
        );
    }

    public static ReservationResponse toReservationResponse(Reservation r) {
        return new ReservationResponse(
                r.getReservationId(),
                r.getUser() != null ? r.getUser().getUserId() : null,
                r.getUser() != null ? r.getUser().getEmail() : null,
                r.getReservationDate(),
                r.getQrId(),
                toReservationItems(r.getReservationStalls())
        );
    }
}