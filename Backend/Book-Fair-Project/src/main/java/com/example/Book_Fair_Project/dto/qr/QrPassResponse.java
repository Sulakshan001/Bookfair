package com.example.Book_Fair_Project.dto.qr;

import java.time.LocalDateTime;

public class QrPassResponse {

    private Long qrId;
    private Long reservationId;
    private String qrCode;
    private boolean active;
    private LocalDateTime generatedAt;
    private LocalDateTime usedAt;

    public QrPassResponse() {}

    public QrPassResponse(Long qrId, Long reservationId, String qrCode, boolean active,
                          LocalDateTime generatedAt, LocalDateTime usedAt) {
        this.qrId = qrId;
        this.reservationId = reservationId;
        this.qrCode = qrCode;
        this.active = active;
        this.generatedAt = generatedAt;
        this.usedAt = usedAt;
    }

    public Long getQrId() { return qrId; }
    public void setQrId(Long qrId) { this.qrId = qrId; }

    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }

    public LocalDateTime getUsedAt() { return usedAt; }
    public void setUsedAt(LocalDateTime usedAt) { this.usedAt = usedAt; }
}

