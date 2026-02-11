package com.example.BookFairpro.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "qr_pass",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_qr_pass_reservation_id", columnNames = "reservation_id")
        }
)
public class QrPass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qr_id")
    private Long qrId;

    @OneToOne(optional = false)
    @JoinColumn(name = "reservation_id", nullable = false, unique = true)
    private Reservation reservation;

    @Column(name = "qr_code", nullable = false, length = 255, unique = true)
    private String qrCode;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "generated_at", updatable = false)
    private LocalDateTime generatedAt;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    public QrPass() {}

    public QrPass(Long qrId, Reservation reservation, String qrCode, boolean active,
                  LocalDateTime generatedAt, LocalDateTime usedAt) {
        this.qrId = qrId;
        this.reservation = reservation;
        this.qrCode = qrCode;
        this.active = active;
        this.generatedAt = generatedAt;
        this.usedAt = usedAt;
    }

    public Long getQrId() { return qrId; }
    public void setQrId(Long qrId) { this.qrId = qrId; }

    public Reservation getReservation() { return reservation; }
    public void setReservation(Reservation reservation) { this.reservation = reservation; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }

    public LocalDateTime getUsedAt() { return usedAt; }
    public void setUsedAt(LocalDateTime usedAt) { this.usedAt = usedAt; }
}
