package com.example.Book_Fair_Project.model;



import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "reservation_date")
    private LocalDateTime reservationDate;

    // In your ER: reservations.qr_id is VARCHAR and noted "not an FK" :contentReference[oaicite:4]{index=4}
    @Column(name = "qr_id", nullable = false, length = 255)
    private String qrId;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationStall> reservationStalls = new ArrayList<>();

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QrPass> qrPasses = new ArrayList<>();

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<EmailNotification> emails = new ArrayList<>();



    public Reservation() {}

    public Reservation(Long reservationId, User user, LocalDateTime reservationDate, String qrId) {
        this.reservationId = reservationId;
        this.user = user;
        this.reservationDate = reservationDate;
        this.qrId = qrId;
    }

    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getReservationDate() { return reservationDate; }
    public void setReservationDate(LocalDateTime reservationDate) { this.reservationDate = reservationDate; }

    public String getQrId() { return qrId; }
    public void setQrId(String qrId) { this.qrId = qrId; }

    public List<ReservationStall> getReservationStalls() { return reservationStalls; }
    public void setReservationStalls(List<ReservationStall> reservationStalls) { this.reservationStalls = reservationStalls; }

    public List<Payment> getPayments() { return payments; }
    public void setPayments(List<Payment> payments) { this.payments = payments; }

    public List<QrPass> getQrPasses() { return qrPasses; }
    public void setQrPasses(List<QrPass> qrPasses) { this.qrPasses = qrPasses; }

    public List<EmailNotification> getEmails() { return emails; }
    public void setEmails(List<EmailNotification> emails) { this.emails = emails; }
}

