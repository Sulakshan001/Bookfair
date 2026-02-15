package com.example.Book_Fair_Project.service;

import com.example.Book_Fair_Project.dto.qr.QrPassResponse;

public interface QrPassService {
    QrPassResponse generateQrPass(Long reservationId);
    QrPassResponse getQrPassByReservation(Long reservationId);
    QrPassResponse markQrAsUsed(Long qrId);
}