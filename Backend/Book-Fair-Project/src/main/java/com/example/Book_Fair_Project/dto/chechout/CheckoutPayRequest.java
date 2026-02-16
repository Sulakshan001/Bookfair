package com.example.Book_Fair_Project.dto.chechout;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CheckoutPayRequest {

    @NotNull
    private Long reservationId;

    @NotNull
    private BigDecimal amount;

    // expected: CARD / CASH / PAYPAL / WALLET / BANK_TRANSFER
    @NotBlank
    private String paymentMethod;

    // any JSON string from frontend (card data, paypal email, etc.)
    private String paymentDetails;

    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentDetails() { return paymentDetails; }
    public void setPaymentDetails(String paymentDetails) { this.paymentDetails = paymentDetails; }
}

