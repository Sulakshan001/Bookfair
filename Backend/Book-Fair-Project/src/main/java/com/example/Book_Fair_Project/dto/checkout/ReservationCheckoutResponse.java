package com.example.Book_Fair_Project.dto.checkout;

import com.example.Book_Fair_Project.dto.payment.PaymentResponse;
import com.example.Book_Fair_Project.dto.qr.QrPassResponse;
import com.example.Book_Fair_Project.dto.reservation.ReservationResponse;

public class ReservationCheckoutResponse {

    private ReservationResponse reservation;
    private PaymentResponse payment;
    private QrPassResponse qr;

    public ReservationCheckoutResponse() {}

    public ReservationCheckoutResponse(ReservationResponse reservation, PaymentResponse payment, QrPassResponse qr) {
        this.reservation = reservation;
        this.payment = payment;
        this.qr = qr;
    }

    public ReservationResponse getReservation() { return reservation; }
    public void setReservation(ReservationResponse reservation) { this.reservation = reservation; }

    public PaymentResponse getPayment() { return payment; }
    public void setPayment(PaymentResponse payment) { this.payment = payment; }

    public QrPassResponse getQr() { return qr; }
    public void setQr(QrPassResponse qr) { this.qr = qr; }
}

