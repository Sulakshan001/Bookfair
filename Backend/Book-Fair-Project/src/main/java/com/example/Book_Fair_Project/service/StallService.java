package com.example.Book_Fair_Project.service;

import com.example.Book_Fair_Project.dto.stall.StallResponse;
import com.example.Book_Fair_Project.model.Stall;

import java.util.List;

public interface StallService {
    List<StallResponse> getAllStalls();
    StallResponse getStallById(Long stallId);
    List<StallResponse> getAvailableStalls();
    StallResponse getStallByCode(String stallCode);
    Stall getStallEntityById(Long stallId);
}
