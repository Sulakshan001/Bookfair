package com.example.Book_Fair_Project.repository;

import com.example.Book_Fair_Project.model.UserGenre;
import com.example.Book_Fair_Project.model.UserGenreId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGenreRepository extends JpaRepository<UserGenre, UserGenreId> {

    List<UserGenre> findByUser_UserId(Long userId);

    void deleteByUser_UserId(Long userId);

    boolean existsById(UserGenreId id);
}

