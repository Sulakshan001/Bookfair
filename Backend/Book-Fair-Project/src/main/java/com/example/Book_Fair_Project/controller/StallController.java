package com.example.Book_Fair_Project.controller;

import com.example.Book_Fair_Project.dto.common.ApiResponse;
import com.example.Book_Fair_Project.dto.stall.StallResponse;
import com.example.Book_Fair_Project.service.StallService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stalls")

public class StallController {

    private final StallService stallService;

    public StallController(StallService stallService) {
        this.stallService = stallService;
    }

    // ✅ Get all stalls
    @GetMapping
    public ResponseEntity<ApiResponse<List<StallResponse>>> getAllStalls() {
        List<StallResponse> stalls = stallService.getAllStalls();
        return ResponseEntity.ok(ApiResponse.ok("Stalls retrieved successfully", stalls, 200));
    }

    // ✅ Get stall by ID
    @GetMapping("/{stallId}")
    public ResponseEntity<ApiResponse<StallResponse>> getStallById(@PathVariable Long stallId) {
        StallResponse stall = stallService.getStallById(stallId);
        return ResponseEntity.ok(ApiResponse.ok("Stall retrieved successfully", stall, 200));
    }

    // ✅ Get available stalls only
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<StallResponse>>> getAvailableStalls() {
        List<StallResponse> stalls = stallService.getAvailableStalls();
        return ResponseEntity.ok(ApiResponse.ok("Available stalls retrieved successfully", stalls, 200));
    }

    // ✅ Get stall by code
    @GetMapping("/code/{stallCode}")
    public ResponseEntity<ApiResponse<StallResponse>> getStallByCode(@PathVariable String stallCode) {
        StallResponse stall = stallService.getStallByCode(stallCode);
        return ResponseEntity.ok(ApiResponse.ok("Stall retrieved successfully", stall, 200));
    }
}
