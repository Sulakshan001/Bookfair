package com.example.Book_Fair_Project.model;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ReservationStallId implements Serializable {

    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "stall_id")
    private Long stallId;

    public ReservationStallId() {}

    public ReservationStallId(Long reservationId, Long stallId) {
        this.reservationId = reservationId;
        this.stallId = stallId;
    }

    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }

    public Long getStallId() { return stallId; }
    public void setStallId(Long stallId) { this.stallId = stallId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationStallId that = (ReservationStallId) o;
        return Objects.equals(reservationId, that.reservationId) &&
                Objects.equals(stallId, that.stallId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId, stallId);
    }
}
