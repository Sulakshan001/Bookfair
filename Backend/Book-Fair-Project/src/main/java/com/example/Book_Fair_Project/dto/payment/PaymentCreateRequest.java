package com.example.Book_Fair_Project.dto.payment;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PaymentCreateRequest {

    @NotNull
    private Long reservationId;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private String paymentMethod; // CARD/WALLET/BANK_TRANSFER/CASH

    private String referenceNumber;
    private String paymentDetails; // JSON string

    public PaymentCreateRequest() {}

    public PaymentCreateRequest(Long reservationId, BigDecimal amount, String paymentMethod,
                                String referenceNumber, String paymentDetails) {
        this.reservationId = reservationId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.referenceNumber = referenceNumber;
        this.paymentDetails = paymentDetails;
    }

    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }

    public String getPaymentDetails() { return paymentDetails; }
    public void setPaymentDetails(String paymentDetails) { this.paymentDetails = paymentDetails; }
}

