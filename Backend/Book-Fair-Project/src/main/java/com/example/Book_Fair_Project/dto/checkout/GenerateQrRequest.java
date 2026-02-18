package com.example.Book_Fair_Project.dto.checkout;

import jakarta.validation.constraints.NotNull;

public class GenerateQrRequest {
    @NotNull
    private Long reservationId;

    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }
}