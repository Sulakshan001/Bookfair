package com.example.Book_Fair_Project.service;

import com.example.Book_Fair_Project.dto.payment.PaymentCreateRequest;
import com.example.Book_Fair_Project.dto.payment.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse processPayment(PaymentCreateRequest request);
    PaymentResponse getPaymentById(Long paymentId);
    List<PaymentResponse> getReservationPayments(Long reservationId);
    PaymentResponse markPaymentSuccessful(Long paymentId, String referenceNumber);
    PaymentResponse markPaymentFailed(Long paymentId);
}
