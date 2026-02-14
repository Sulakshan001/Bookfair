package com.example.Book_Fair_Project.dto.reservation;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ReservationCreateRequest {

    @NotNull
    private Long userId;

    @NotEmpty
    private List<Long> stallIds;

    @NotNull
    private String qrId; // you store qr_id varchar in reservations (not FK)

    public ReservationCreateRequest() {}

    public ReservationCreateRequest(Long userId, List<Long> stallIds, String qrId) {
        this.userId = userId;
        this.stallIds = stallIds;
        this.qrId = qrId;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<Long> getStallIds() { return stallIds; }
    public void setStallIds(List<Long> stallIds) { this.stallIds = stallIds; }

    public String getQrId() { return qrId; }
    public void setQrId(String qrId) { this.qrId = qrId; }
}

