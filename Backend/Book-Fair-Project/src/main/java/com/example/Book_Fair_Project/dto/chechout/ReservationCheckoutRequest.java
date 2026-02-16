package com.example.Book_Fair_Project.dto.chechout;

import java.util.List;

public class ReservationCheckoutRequest {
    private Long userId;
    private List<Long> stallIds;
    private List<String> genres;

    private String paymentMethod;   // CASH/CARD/WALLET/BANK_TRANSFER
    private String paymentDetails;  // optional

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<Long> getStallIds() { return stallIds; }
    public void setStallIds(List<Long> stallIds) { this.stallIds = stallIds; }

    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentDetails() { return paymentDetails; }
    public void setPaymentDetails(String paymentDetails) { this.paymentDetails = paymentDetails; }
}