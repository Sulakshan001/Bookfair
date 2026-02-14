package com.example.Book_Fair_Project.dto.reservation;



import java.time.LocalDateTime;
import java.util.List;

public class ReservationResponse {

    private Long reservationId;
    private Long userId;
    private String userEmail;
    private LocalDateTime reservationDate;
    private String qrId;
    private List<ReservationItemResponse> stalls;

    public ReservationResponse() {}

    public ReservationResponse(Long reservationId, Long userId, String userEmail,
                               LocalDateTime reservationDate, String qrId,
                               List<ReservationItemResponse> stalls) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.userEmail = userEmail;
        this.reservationDate = reservationDate;
        this.qrId = qrId;
        this.stalls = stalls;
    }

    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public LocalDateTime getReservationDate() { return reservationDate; }
    public void setReservationDate(LocalDateTime reservationDate) { this.reservationDate = reservationDate; }

    public String getQrId() { return qrId; }
    public void setQrId(String qrId) { this.qrId = qrId; }

    public List<ReservationItemResponse> getStalls() { return stalls; }
    public void setStalls(List<ReservationItemResponse> stalls) { this.stalls = stalls; }
}
