package com.example.Book_Fair_Project.controller;


import com.example.Book_Fair_Project.dto.checkout.ReservationCheckoutRequest;
import com.example.Book_Fair_Project.dto.checkout.ReservationCheckoutResponse;
import com.example.Book_Fair_Project.dto.payment.PaymentCreateRequest;
import com.example.Book_Fair_Project.dto.payment.PaymentResponse;
import com.example.Book_Fair_Project.dto.qr.QrPassResponse;
import com.example.Book_Fair_Project.dto.reservation.ReservationResponse;
import com.example.Book_Fair_Project.service.CheckoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    // ✅ ALL IN ONE: reserve + pay + qr
    @PostMapping("/reserve-pay-qr")
    public ResponseEntity<ReservationCheckoutResponse> reservePayQr(
            @RequestBody ReservationCheckoutRequest request
    ) {
        return ResponseEntity.ok(checkoutService.reservePayAndGenerateQr(request));
    }

    // ✅ reserve only
    @PostMapping("/reserve")
    public ResponseEntity<ReservationResponse> reserve(
            @RequestBody ReservationCheckoutRequest request
    ) {
        return ResponseEntity.ok(checkoutService.reserve(request));
    }

    // ✅ pay only (stores payment_details JSON in DB)
    @PostMapping("/pay")
    public ResponseEntity<PaymentResponse> pay(
            @RequestBody PaymentCreateRequest request
    ) {
        return ResponseEntity.ok(checkoutService.pay(request));
    }

    // ✅ generate QR only
    @PostMapping("/generate-qr")
    public ResponseEntity<QrPassResponse> generateQr(@RequestBody Map<String, Object> request) {
        Long reservationId = ((Number) request.get("reservationId")).longValue();
        return ResponseEntity.ok(checkoutService.generateQr(reservationId));
    }
}