package com.example.Book_Fair_Project.service;

import com.example.Book_Fair_Project.dto.reservation.ReservationCreateRequest;
import com.example.Book_Fair_Project.dto.reservation.ReservationResponse;

import java.util.List;

public interface ReservationService {
    ReservationResponse createReservation(ReservationCreateRequest request);
    ReservationResponse getReservationById(Long reservationId);
    List<ReservationResponse> getUserReservations(Long userId);
    List<ReservationResponse> getAllReservations();
    ReservationResponse cancelReservation(Long reservationId);
}
