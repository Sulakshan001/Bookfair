package com.example.Book_Fair_Project.service;

import com.example.Book_Fair_Project.dto.stall.StallResponse;
import com.example.Book_Fair_Project.exception.NotFoundException;
import com.example.Book_Fair_Project.mapper.DtoMapper;
import com.example.Book_Fair_Project.model.Stall;
import com.example.Book_Fair_Project.repository.StallRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StallServiceImpl implements StallService {

    private final StallRepository stallRepository;

    public StallServiceImpl(StallRepository stallRepository) {
        this.stallRepository = stallRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StallResponse> getAllStalls() {
        return stallRepository.findAll().stream()
                .map(DtoMapper::toStallResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public StallResponse getStallById(Long stallId) {
        Stall stall = stallRepository.findById(stallId)
                .orElseThrow(() -> new NotFoundException("Stall not found: " + stallId));
        return DtoMapper.toStallResponse(stall);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StallResponse> getAvailableStalls() {
        return stallRepository.findByStatus(Stall.Status.AVAILABLE).stream()
                .map(DtoMapper::toStallResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public StallResponse getStallByCode(String stallCode) {
        Stall stall = stallRepository.findByStallCode(stallCode)
                .orElseThrow(() -> new NotFoundException("Stall not found with code: " + stallCode));
        return DtoMapper.toStallResponse(stall);
    }

    @Override
    @Transactional(readOnly = true)
    public Stall getStallEntityById(Long stallId) {
        return stallRepository.findById(stallId)
                .orElseThrow(() -> new NotFoundException("Stall not found: " + stallId));
    }
}
