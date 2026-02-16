package com.example.Book_Fair_Project.dto.chechout;

public class CheckoutPayResponse {
    private Long paymentId;
    private String paymentStatus; // SUCCESS / FAILED / PENDING
    private String referenceNumber;

    public CheckoutPayResponse() {}

    public CheckoutPayResponse(Long paymentId, String paymentStatus, String referenceNumber) {
        this.paymentId = paymentId;
        this.paymentStatus = paymentStatus;
        this.referenceNumber = referenceNumber;
    }

    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
}
