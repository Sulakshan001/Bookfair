package com.example.Book_Fair_Project.repository;


import com.example.Book_Fair_Project.model.Stall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StallRepository extends JpaRepository<Stall, Long> {
    Optional<Stall> findByStallCode(String stallCode);
    boolean existsByStallCode(String stallCode);
    List<Stall> findByStatus(Stall.Status status);
}
