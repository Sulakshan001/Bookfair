package com.example.Book_Fair_Project.service;

import com.example.Book_Fair_Project.dto.checkout.ReservationCheckoutRequest;
import com.example.Book_Fair_Project.dto.checkout.ReservationCheckoutResponse;
import com.example.Book_Fair_Project.dto.payment.PaymentCreateRequest;
import com.example.Book_Fair_Project.dto.payment.PaymentResponse;
import com.example.Book_Fair_Project.dto.qr.QrPassResponse;
import com.example.Book_Fair_Project.dto.reservation.ReservationResponse;

public interface CheckoutService {
    ReservationCheckoutResponse reservePayAndGenerateQr(ReservationCheckoutRequest request);
    ReservationResponse reserve(ReservationCheckoutRequest request);
    PaymentResponse pay(PaymentCreateRequest request);
    QrPassResponse generateQr(Long reservationId);


}
