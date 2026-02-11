package com.example.BookFairpro.model;


import jakarta.persistence.*;

@Entity
@Table(name = "reservation_stalls")
public class ReservationStall {

    @EmbeddedId
    private ReservationStallId id;

    @ManyToOne(optional = false)
    @MapsId("reservationId")
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne(optional = false)
    @MapsId("stallId")
    @JoinColumn(name = "stall_id", nullable = false)
    private Stall stall;

    public ReservationStall() {}

    public ReservationStall(ReservationStallId id, Reservation reservation, Stall stall) {
        this.id = id;
        this.reservation = reservation;
        this.stall = stall;
    }

    public ReservationStallId getId() { return id; }
    public void setId(ReservationStallId id) { this.id = id; }

    public Reservation getReservation() { return reservation; }
    public void setReservation(Reservation reservation) { this.reservation = reservation; }

    public Stall getStall() { return stall; }
    public void setStall(Stall stall) { this.stall = stall; }
}
