package com.example.Book_Fair_Project.repository;

import com.example.Book_Fair_Project.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByReservation_ReservationId(Long reservationId);
    List<Payment> findByPaymentStatus(Payment.PaymentStatus paymentStatus);
}


