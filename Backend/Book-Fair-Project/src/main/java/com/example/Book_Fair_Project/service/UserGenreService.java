package com.example.Book_Fair_Project.service;

import com.example.Book_Fair_Project.dto.genre.SaveUserGenresRequest;
import com.example.Book_Fair_Project.dto.genre.UserGenresResponse;

import java.util.List;

public interface UserGenreService {

    // Replace all user genres with the provided list (most common + simplest)
    UserGenresResponse replaceUserGenres(SaveUserGenresRequest request);
    void replaceGenres(Long userId, List<String> genres);

    // Add (merge) genres without deleting old ones
    UserGenresResponse addUserGenres(SaveUserGenresRequest request);

    // Remove a single genre
    void removeUserGenre(Long userId, String genreName);

    // Get user genres
    UserGenresResponse getUserGenres(Long userId);

    // Delete all genres for user
    void clearUserGenres(Long userId);
}