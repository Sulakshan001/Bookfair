package com.example.Book_Fair_Project.repository;

import com.example.Book_Fair_Project.model.ReservationStall;
import com.example.Book_Fair_Project.model.ReservationStallId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationStallRepository extends JpaRepository<ReservationStall, ReservationStallId> {
    List<ReservationStall> findByReservation_ReservationId(Long reservationId);
    List<ReservationStall> findByStall_StallId(Long stallId);
}

