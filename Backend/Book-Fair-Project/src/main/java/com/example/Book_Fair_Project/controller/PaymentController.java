package com.example.Book_Fair_Project.controller;

import com.example.Book_Fair_Project.dto.common.ApiResponse;
import com.example.Book_Fair_Project.dto.payment.PaymentCreateRequest;
import com.example.Book_Fair_Project.dto.payment.PaymentResponse;
import com.example.Book_Fair_Project.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")

public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // ✅ Process payment
    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(
            @Valid @RequestBody PaymentCreateRequest request) {
        PaymentResponse payment = paymentService.processPayment(request);
        return ResponseEntity.status(201)
                .body(ApiResponse.ok("Payment processed successfully", payment, 201));
    }

    // ✅ Get payment by ID
    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentById(@PathVariable Long paymentId) {
        PaymentResponse payment = paymentService.getPaymentById(paymentId);
        return ResponseEntity.ok(ApiResponse.ok("Payment retrieved successfully", payment, 200));
    }

    // ✅ Get payments for reservation
    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getReservationPayments(@PathVariable Long reservationId) {
        List<PaymentResponse> payments = paymentService.getReservationPayments(reservationId);
        return ResponseEntity.ok(ApiResponse.ok("Reservation payments retrieved successfully", payments, 200));
    }

    // ✅ Mark payment as successful
    @PutMapping("/{paymentId}/success")
    public ResponseEntity<ApiResponse<PaymentResponse>> markPaymentSuccessful(
            @PathVariable Long paymentId,
            @RequestParam String referenceNumber) {
        PaymentResponse payment = paymentService.markPaymentSuccessful(paymentId, referenceNumber);
        return ResponseEntity.ok(ApiResponse.ok("Payment marked as successful", payment, 200));
    }

    // ✅ Mark payment as failed
    @PutMapping("/{paymentId}/failed")
    public ResponseEntity<ApiResponse<PaymentResponse>> markPaymentFailed(@PathVariable Long paymentId) {
        PaymentResponse payment = paymentService.markPaymentFailed(paymentId);
        return ResponseEntity.ok(ApiResponse.ok("Payment marked as failed", payment, 200));
    }
}

