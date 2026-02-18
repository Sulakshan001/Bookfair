package com.example.Book_Fair_Project.repository;

import com.example.Book_Fair_Project.model.QrPass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QrPassRepository extends JpaRepository<QrPass, Long> {

    List<QrPass> findByActiveTrue();
    boolean existsByReservation_ReservationId(Long reservationId);
    Optional<QrPass> findByReservation_ReservationId(Long reservationId);

    Optional<QrPass> findByQrCode(String qrCode);

    Page<QrPass> findAllByActiveTrue(Pageable pageable);

    Page<QrPass> findAllByActiveFalse(Pageable pageable);

    long countByActiveTrue();

    long countByActiveFalse();
}
