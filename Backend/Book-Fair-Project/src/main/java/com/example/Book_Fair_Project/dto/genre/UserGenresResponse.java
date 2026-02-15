package com.example.Book_Fair_Project.dto.genre;

import java.util.List;

public class UserGenresResponse {

    private Long userId;
    private List<String> genres;

    public UserGenresResponse() {}

    public UserGenresResponse(Long userId, List<String> genres) {
        this.userId = userId;
        this.genres = genres;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }
}


