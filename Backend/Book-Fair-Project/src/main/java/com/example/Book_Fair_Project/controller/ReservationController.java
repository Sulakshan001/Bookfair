package com.example.Book_Fair_Project.controller;

import com.example.Book_Fair_Project.dto.common.ApiResponse;
import com.example.Book_Fair_Project.dto.reservation.ReservationCreateRequest;
import com.example.Book_Fair_Project.dto.reservation.ReservationResponse;
import com.example.Book_Fair_Project.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")

public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // ✅ Create reservation
    @PostMapping
    public ResponseEntity<ApiResponse<ReservationResponse>> createReservation(
            @Valid @RequestBody ReservationCreateRequest request) {
        ReservationResponse reservation = reservationService.createReservation(request);
        return ResponseEntity.status(201)
                .body(ApiResponse.ok("Reservation created successfully", reservation, 201));
    }

    // ✅ Get reservation by ID
    @GetMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationResponse>> getReservationById(@PathVariable Long reservationId) {
        ReservationResponse reservation = reservationService.getReservationById(reservationId);
        return ResponseEntity.ok(ApiResponse.ok("Reservation retrieved successfully", reservation, 200));
    }

    // ✅ Get user's reservations
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getUserReservations(@PathVariable Long userId) {
        List<ReservationResponse> reservations = reservationService.getUserReservations(userId);
        return ResponseEntity.ok(ApiResponse.ok("User reservations retrieved successfully", reservations, 200));
    }

    // ✅ Get all reservations (admin)
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getAllReservations() {
        List<ReservationResponse> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(ApiResponse.ok("All reservations retrieved successfully", reservations, 200));
    }

    // ✅ Cancel reservation
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<Void>> cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.ok(ApiResponse.ok("Reservation cancelled successfully", null, 200));
    }
}
