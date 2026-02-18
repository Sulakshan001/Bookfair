package com.example.Book_Fair_Project.controller;

import com.example.Book_Fair_Project.dto.qr.QrPassResponse;
import com.example.Book_Fair_Project.service.QrPassService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qr")
public class QrPassController {

    private final QrPassService qrPassService;

    public QrPassController(QrPassService qrPassService) {
        this.qrPassService = qrPassService;
    }

    // Generate and send email to Vendor & Publisher
    @PostMapping("/generate/{reservationId}")
    public ResponseEntity<QrPassResponse> generate(@PathVariable Long reservationId) {
        return ResponseEntity.ok(qrPassService.generateQrPass(reservationId));
    }

    // Get QR by reservation
    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<QrPassResponse> getByReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(qrPassService.getQrPassByReservation(reservationId));
    }

    // Mark QR used (gate scan)
    @PutMapping("/use/{qrId}")
    public ResponseEntity<QrPassResponse> markUsed(@PathVariable Long qrId) {
        return ResponseEntity.ok(qrPassService.markQrAsUsed(qrId));
    }
}
