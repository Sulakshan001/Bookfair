package com.example.Book_Fair_Project.dto.payment;

import jakarta.validation.constraints.NotNull;

public class PaymentUpdateStatusRequest {

    @NotNull
    private String paymentStatus; // PENDING/SUCCESS/FAILED

    public PaymentUpdateStatusRequest() {}

    public PaymentUpdateStatusRequest(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}

